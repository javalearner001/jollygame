package com.sun.jollygame.singlesource;

import java.util.ArrayList;
import java.util.List;

public class ImgIdFactory {

    private static final ImgIdFactory imgFactory = new ImgIdFactory();

    private List<Integer> imgIdList = new ArrayList<>(10);

    private ImgIdFactory(){

    }

    public static ImgIdFactory getInstance(){
        return imgFactory;
    }

    public void updateImgIdList(List<Integer> imgIdList){
        this.imgIdList = imgIdList;
    }

    public List<Integer> getImgList(){
        return this.imgIdList;
    }
}
