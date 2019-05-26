package com.mmjang.ankillusion.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private static Settings settings = null;

    private final static String PREFER_NAME = "settings";    //应用设置名称
    private final static String MODEL_ID = "model_id";       //应用设置项 模版id
    private final static String DECK_NAME = "deck_name";         //应用设置项 牌组id
    // 0 hide all reveal all
    // 1 hide one reveal one
    // 2 hide all reveal one
    private final static String CREATION_MODE = "creation_mode";
    private final static String OCCLUSION_COLOR = "occlusion_color";
    private final static String OCCLUSION_COLOR_HIGHLIGHT = "occlusion_color_highlight";
    private final static String ABORT_AFTER_SUCCESS = "abort_after_success";

    private final static String ALL_TAGS = "all_tags";
    private final static String LAST_TAGS = "last_tags";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    private Settings(Context context) {
        sp = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 获得单例
     *
     * @return
     */
    public static Settings getInstance(Context context) {
        if (settings == null) {
            settings = new Settings(context);
        }
        return settings;
    }

    /*************/

    public Long getModelId() {
        return sp.getLong(MODEL_ID, 0);
    }

    public void setModelId(Long modelId) {
        editor.putLong(MODEL_ID, modelId);
        editor.commit();
    }

    /**************/

    public String getDeckName() {
        return sp.getString(DECK_NAME, "");
    }

    public void setDeckName(String deckName) {
        editor.putString(DECK_NAME, deckName);
        editor.commit();
    }

    /*************/
    public String getOcclusionColor(){
        return sp.getString(OCCLUSION_COLOR, Constant.DEFAULT_OCCLUSION_COLOR);
    }

    public void setOcclusionColor(String occlusionColor){
        editor.putString(OCCLUSION_COLOR, occlusionColor);
        editor.commit();
    }
    /*************/
    public String getOcclusionColorHighlight(){
        return sp.getString(OCCLUSION_COLOR_HIGHLIGHT, Constant.DEFAULT_OCCLUSION_COLOR_HIGHLIGHT);
    }

    public void setOcclusionColorHighlight(String occlusionColorHighlight){
        editor.putString(OCCLUSION_COLOR_HIGHLIGHT, occlusionColorHighlight);
        editor.commit();
    }

    /************/
    public int getCreationMode(){
        return sp.getInt(CREATION_MODE, 0);
    }

    public void setCreationMode(int creationMode){
        editor.putInt(CREATION_MODE, creationMode);
        editor.commit();
    }

    /***********/
    public boolean getAbortAfterSuccess(){
        return sp.getBoolean(ABORT_AFTER_SUCCESS, true);
    }

    public void setAbortAfterSuccess(boolean abortAfterSuccess){
        editor.putBoolean(ABORT_AFTER_SUCCESS, abortAfterSuccess);
        editor.commit();
    }

    /************/
    public List<String> getAllTags(){
        return string2list(sp.getString(ALL_TAGS, ""));
    }

    public void setAllTags(List<String> allTags){
        editor.putString(ALL_TAGS, list2string(allTags));
        editor.commit();
    }

    /***********/
    public List<String> getLastTags(){
        return string2list(sp.getString(LAST_TAGS, ""));
    }

    public void setLastTags(List<String> allTags){
        editor.putString(LAST_TAGS, list2string(allTags));
        editor.commit();
    }

    /***********/

    public boolean hasKey(String key) {
        return sp.contains(key);
    }

    /************/
    public static List<String> string2list(String s){
        List<String> result = new ArrayList<>();
        for(String c : s.split(" ")){
            String trimmed = c.trim();
            if(!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    public static String list2string(List<String> list){
        if(list.size() == 0){
            return "";
        }
        if(list.size() == 1){
            return list.get(0);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0).trim());
        for(int i = 1; i < list.size(); i ++){
            sb.append(" ");
            sb.append(list.get(i).trim());
        }
        return sb.toString();
    }
    /************/
}

