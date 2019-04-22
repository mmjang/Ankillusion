package com.mmjang.ankillusion.data;

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
        List<OcclusionObject> occlusionObjectList = new ArrayList<>();
        if(expType == OcclusionExportType.HIDE_ALL_REVEAL_ALL){
            for(OcclusionItem occlusionItem : occlusionItemList){
                occlusionItem.highlight = true;
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
            for(OcclusionItem occlusionItem : occlusionItemList){
                occlusionItem.highlight = true;
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
            //todo: finish this
        }

        return occlusionObjectList;
    }
}
