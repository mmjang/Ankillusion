package com.mmjang.ankillusion.data;

import com.mmjang.ankillusion.MyApplication;

import java.util.ArrayList;
import java.util.List;

import cn.hzw.doodle.occlusion.OcclusionItem;

public class OcclusionObjectListGenerator {
    public static List<OcclusionObject> gen(
            int version,
            String img,
            int width,
            int height,
            List<OcclusionItem> occlusionItemList,
            OcclusionExportType expType
    ){
        Settings mySettings = Settings.getInstance(MyApplication.getContext());
        List<OcclusionObject> occlusionObjectList = new ArrayList<>();
        if(expType == OcclusionExportType.HIDE_ALL_REVEAL_ALL){
            for(OcclusionItem occlusionItem : occlusionItemList){
                occlusionItem.highlight = true;
                occlusionItem.color = mySettings.getOcclusionColorHighlight();
            }
            List<OcclusionItem> frontList = occlusionItemList;
            List<OcclusionItem> backList = new ArrayList<>();
            OcclusionObject occlusionObject = new OcclusionObject(
                    version,
                    img,
                    width,
                    height,
                    frontList,
                    backList
            );
            occlusionObjectList.add(occlusionObject);
        }
        if(expType == OcclusionExportType.HIDE_ONE_REVEAL_ONE){
            //when no occlusion, make one card.
            if(occlusionItemList.size() == 0){
                List<OcclusionItem> frontList = new ArrayList<>();
                List<OcclusionItem> backList = new ArrayList<>();
                OcclusionObject occlusionObject = new OcclusionObject(
                        version,
                        img,
                        width,
                        height,
                        frontList,
                        backList
                );
                occlusionObjectList.add(occlusionObject);
            }

            for(OcclusionItem occlusionItem : occlusionItemList){
                occlusionItem.highlight = true;
                occlusionItem.color = mySettings.getOcclusionColorHighlight();
                List<OcclusionItem> frontList = new ArrayList<>();
                frontList.add(occlusionItem);
                List<OcclusionItem> backList = new ArrayList<>();
                OcclusionObject occlusionObject = new OcclusionObject(
                        version,
                        img,
                        width,
                        height,
                        frontList,
                        backList
                );
                occlusionObjectList.add(occlusionObject);
            }
        }
        if(expType == OcclusionExportType.HIDE_ALL_REVEAL_ONE){

            //when no occlusion, make one card.
            if(occlusionItemList.size() == 0){
                List<OcclusionItem> frontList = new ArrayList<>();
                List<OcclusionItem> backList = new ArrayList<>();
                OcclusionObject occlusionObject = new OcclusionObject(
                        version,
                        img,
                        width,
                        height,
                        frontList,
                        backList
                );
                occlusionObjectList.add(occlusionObject);
            }

            for(int i = 0; i < occlusionItemList.size(); i ++){
                List<OcclusionItem> frontList = new ArrayList<>();
                List<OcclusionItem> backList = new ArrayList<>();
                for(int j = 0; j < occlusionItemList.size(); j ++){
                    OcclusionItem copiedItem = occlusionItemList.get(j).clone();
                    if(j == i){//this is the item to hide
                        copiedItem.highlight = true;
                        copiedItem.color = mySettings.getOcclusionColorHighlight();
                        frontList.add(copiedItem);
                    }else{
                        frontList.add(copiedItem);
                        backList.add(copiedItem.clone());
                    }
                }
                OcclusionObject occlusionObject = new OcclusionObject(
                        version,
                        img,
                        width,
                        height,
                        frontList,
                        backList
                );
                occlusionObjectList.add(occlusionObject);
            }
        }

        return occlusionObjectList;
    }
}
