package cn.hzw.doodle.occlusion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OcclusionItem {
    public enum Type {
        Rect,
        Circle,
        Line
    }

    public Type type;
    public String color;
    public double[][] data;
    public boolean highlight;

    public OcclusionItem clone(){
        OcclusionItem newItem = new OcclusionItem();
        newItem.type = type;
        newItem.color = color + "";
        newItem.highlight = highlight;
        newItem.data = data;
        //don't clone data array to avoid large memory consumption
//        newItem.data = new double[data.length][];
//        for(int i = 0; i < data.length; i ++){
//            newItem.data[i] = data[i].clone();
//        }
        return newItem;
    }

    public JSONObject toJsonObject() throws JSONException{
        JSONObject itemJson = new JSONObject();
        if(type == Type.Rect){
            itemJson.put("type", "rect");
        }
        if(type == Type.Circle){
            itemJson.put("type", "circle");
        }
        if(type == Type.Line){
            itemJson.put("type", "line");
        }
        itemJson.put("color", color);
        itemJson.put("highlight", highlight);
        JSONArray dataJsonArray = new JSONArray();
        for(double[] arr : data){
            JSONArray subArray = new JSONArray();
            for(double d : arr){
                subArray.put(d);
            }
            dataJsonArray.put(subArray);
        }
        itemJson.put("data", dataJsonArray);
        return itemJson;
    }
}
