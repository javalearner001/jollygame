package com.sun.jollygame.job;

import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.HeartBeat;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.response.BoolResponse;
import com.sun.jollygame.entity.response.GameOverResponse;
import com.sun.jollygame.entity.response.MatchResponse;
import com.sun.jollygame.entity.response.MessageResponse;
import com.sun.jollygame.factory.HeartBeatMapFactory;
import com.sun.jollygame.singlesource.ImgIdFactory;
import com.sun.jollygame.singlesource.UserMapFactory;
import com.sun.jollygame.socket.SessionQueue;
import com.sun.jollygame.socket.WebSocket;
import com.sun.jollygame.socketservice.GameRoomService;
import com.sun.jollygame.util.GameRoomUtil;
import com.sun.jollygame.util.GameUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时任务
 *
 * @author sunkai
 * @since 2021/11/25 11:08 上午
 */
@Slf4j
@Component
public class ScheduleTask {
    public static final String format = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat formatter = new SimpleDateFormat(format);
    private static final int outTime = 3;
    private static final int playTime = 10;

    @Autowired
    private GameRoomService gameRoomService;

    /**
     * 每1秒执行一次心跳，扫描内存房间的用户
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void printTest() {
        HeartBeatMapFactory beatMapFactory = HeartBeatMapFactory.getInstance();

        List<String> deleteUserList = new ArrayList<>();
        log.info("心跳map:{}",beatMapFactory.getHeartBeatMap().size());
        for (Map.Entry<String, HeartBeat> entry : beatMapFactory.getHeartBeatMap().entrySet()) {
            HeartBeat heartBeat = entry.getValue();
            Date now = new Date();
            //1.判断时间是否超过限时，超过判断输赢，发消息
            if ((now.getTime() - heartBeat.getCreateTime().getTime()) / 1000 > playTime) {
                GameRoom gameRoom = GameRoomUtil.getGameRoomByUserId(heartBeat.getUserId());
                if (gameRoom != null){
                    checkWin(heartBeat);
                }
                //结束游戏
                gameRoomService.gameOver(heartBeat.getUserId());
                break;
            }

            //调用发送接口
            this.sendHeartBeat(heartBeat.getUserId());
            if (heartBeat.getFirstSendTime() == null) {
                heartBeat.setFirstSendTime(new Date());
                continue;
            }

            Date compareTime = heartBeat.getHeartTime();
            if (compareTime == null) {
                compareTime = heartBeat.getFirstSendTime();
            }

            //1.判断当前是否已断线，或者，已超时
            if ((now.getTime() - compareTime.getTime()) / 1000 > outTime) {
                //删除
                deleteUserList.add(heartBeat.getUserId());
            }
        }

        if (CollectionUtils.isNotEmpty(deleteUserList)) {
            log.info("本次singlelist为：{}", JSON.toJSONString(deleteUserList));
            for (String userId : deleteUserList) {
                gameRoomService.deleteGame(userId);
                MatchResponse matchResponse = new MatchResponse();
                matchResponse.setMessageType(MessageTypeEnum.ONLINE_OUT_RESP.getCode());
                matchResponse.setSingle(true);
                WebSocket.webSocketMap.get(userId).sendObjMessage(JSON.toJSONString(matchResponse));
            }
        }

    }

    /**
     * 每5秒生成随机图片
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void getImgId() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < 101; i++) {
            list.add(i);
        }
        List<Integer> imgIdList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {//显示数字并将其从列表中删除,从而实现不重复.
            imgIdList.add(list.remove(new Random().nextInt(list.size())));
        }

        ImgIdFactory instance = ImgIdFactory.getInstance();
        instance.updateImgIdList(imgIdList);
    }

    /**
     * 每s判断 匹配队列是否只有一人
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void checkSingleUser() {
        SessionQueue sessionQueue = SessionQueue.getSessionQueue();
        log.info("开始判断队列 size:{}", sessionQueue.size());
        if (sessionQueue.size() == 1) {
            String userId = sessionQueue.consume();
            UserMapFactory mapFactory = UserMapFactory.getInstance();
            Date matchTime = mapFactory.get(userId).getMatchTime();
            if ((new Date().getTime() - matchTime.getTime()) / 1000 > 5) {
                MatchResponse matchResponse = new MatchResponse();
                matchResponse.setMessageType(MessageTypeEnum.MATCH_OPPONENT_FAIL.getCode());
                matchResponse.setSingle(true);
                WebSocket.webSocketMap.get(userId).sendObjMessage(JSON.toJSONString(matchResponse));
            } else {
                try {
                    sessionQueue.produce(userId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendHeartBeat(String userId) {
        MessageResponse response = new MessageResponse("心跳消息", MessageTypeEnum.CHECK_BOARD_DATA.getCode());
        WebSocket.webSocketMap.get(userId).sendObjMessage(JSON.toJSONString(response));
    }

    private void checkWin(HeartBeat heartBeat){
        String userId = heartBeat.getUserId();
        GameRoom gameRoom = GameRoomUtil.getGameRoomByUserId(userId);
        if (gameRoom == null) {
            log.error("游戏房间为空，userId:{}", userId);
            return;
        }

        GameOverResponse responseOne = GameUtil.getGameOverResponse(gameRoom.getUserGameRecordFirst());
        GameOverResponse responseTwo = GameUtil.getGameOverResponse(gameRoom.getUserGameRecordSecond());
        if (gameRoom.getUserGameRecordFirst().getGrade() > gameRoom.getUserGameRecordSecond().getGrade()){
            responseOne.setIsWin(1);
            responseTwo.setIsWin(2);
        }else if (gameRoom.getUserGameRecordFirst().getGrade() < gameRoom.getUserGameRecordSecond().getGrade()){
            responseOne.setIsWin(2);
            responseTwo.setIsWin(1);
        }
        WebSocket.webSocketMap.get(responseOne.getUserId()).sendObjMessage(JSON.toJSONString(responseOne));
        WebSocket.webSocketMap.get(responseTwo.getUserId()).sendObjMessage(JSON.toJSONString(responseTwo));
    }
}
