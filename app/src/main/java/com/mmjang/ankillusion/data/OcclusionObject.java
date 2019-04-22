package com.mmjang.ankillusion.data;

import java.util.ArrayList;
import java.util.List;

import cn.hzw.doodle.occlusion.OcclusionItem;

public class OcclusionObject{
    private int version;
    private String imageFile;
    private int width;
    private int height;
    private List<OcclusionItem> shapeListFront;
    private List<OcclusionItem> shapeListBack;

    public OcclusionObject(int version, String imageFile, int width, int height,
                           List<OcclusionItem> shapeListFront, List<OcclusionItem> shapeListBack) {
        this.version = version;
        this.imageFile = imageFile;
        this.width = width;
        this.height = height;
        this.shapeListFront = shapeListFront;
        this.shapeListBack = shapeListBack;
    }

    public OcclusionObject clone(){
        List<OcclusionItem> newFront = new ArrayList<>();
        List<OcclusionItem> newBack = new ArrayList<>();
        for(OcclusionItem item : shapeListFront){
            newFront.add(item.clone());
        }
        for(OcclusionItem item : shapeListBack){
            newBack.add(item.clone());
        }
        OcclusionObject newObj = new OcclusionObject(
            version,
            imageFile + "",
            width,
            height,
                newFront,
                newBack
        );
        return newObj;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<OcclusionItem> getShapeListFront() {
        return shapeListFront;
    }

    public void setShapeListFront(List<OcclusionItem> shapeListFront) {
        this.shapeListFront = shapeListFront;
    }

    public List<OcclusionItem> getShapeListBack() {
        return shapeListBack;
    }

    public void setShapeListBack(List<OcclusionItem> shapeListBack) {
        this.shapeListBack = shapeListBack;
    }
}
