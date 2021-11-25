package com.sun.jollygame.job;

import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.HeartBeat;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.response.MessageResponse;
import com.sun.jollygame.factory.HeartBeatMapFactory;
import com.sun.jollygame.socket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private static final int outTime = 10;
    private static final int playTime = 60;


    /**
     * 每秒执行一次心跳，扫描内存房间的用户
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void printTest(){
        HeartBeatMapFactory beatMapFactory = HeartBeatMapFactory.getInstance();

        List<String> deleteUserList = new ArrayList<>();
        for (Map.Entry<String, HeartBeat> entry : beatMapFactory.getHeartBeatMap().entrySet()){
            log.info("开始发送心跳 userId:{}",entry.getKey());
            HeartBeat heartBeat = entry.getValue();
            log.info("heartBeat值为：{}",JSON.toJSONString(heartBeat));
            //调用发送接口
            this.sendHeartBeat(heartBeat.getUserId());
            if (heartBeat.getFirstSendTime() == null){
                heartBeat.setFirstSendTime(new Date());
                continue;
            }

            Date compareTime = heartBeat.getHeartTime();
            if (compareTime == null){
                compareTime = heartBeat.getFirstSendTime();
            }

            //1.判断当前是否已断线，或者，已超时
            Date now = new Date();
            if ((now.getTime() - compareTime.getTime()) / 1000 > outTime){
                //删除
                deleteUserList.add(heartBeat.getUserId());
            }

            //2.判断是否已经超出对局时间
            if ((now.getTime() - heartBeat.getFirstSendTime().getTime()) / 1000 > playTime + outTime){
                //删除
                deleteUserList.add(heartBeat.getUserId());
            }
        }

        if (CollectionUtils.isNotEmpty(deleteUserList)){
            log.info("本次删除为：{}", JSON.toJSONString(deleteUserList));
            deleteUserList.forEach(x -> beatMapFactory.getHeartBeatMap().remove(x));
        }

    }

    private void sendHeartBeat(String userId) {
        MessageResponse response = new MessageResponse("心跳消息", MessageTypeEnum.CHECK_BOARD_DATA.getCode());
        WebSocket.webSocketMap.get(userId).sendObjMessage(response);
    }
}
