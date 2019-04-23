package com.mmjang.ankillusion.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;

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
        return sp.getString(OCCLUSION_COLOR, Constant.DEFAULT_OCCLUSION_COLOR_HIGHLIGHT);
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

    /************/
    public boolean hasKey(String key) {
        return sp.contains(key);
    }
}

