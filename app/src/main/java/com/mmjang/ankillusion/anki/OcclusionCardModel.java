package com.mmjang.ankillusion.anki;

import android.content.Context;

import com.mmjang.ankillusion.data.Constant;
import com.mmjang.ankillusion.data.Settings;

import java.io.IOException;
import java.io.InputStream;

public class OcclusionCardModel {
    private final AnkiDroidHelper mAnkidroid;
    private Context context;

    private final String defaultModelName = Constant.OCCLUSION_MODEL_NAME;
    private final String MODEL_FILE = "occlusion_model.html";
    private final String MODEL_SPLITTER = "@@@";
    private final String CODING = "UTF-8";
    private final int NUMBER_OF_MODEL_STRING = 3;

    private String[] front = new String[1];
    private String css = "";
    private String[] back = new String[1];

    String[] QFMT = new String[1];
    String[] AFMT = new String[1];
    String[] Cards = {"recite", "type"};
    String CSS;
    public static final String [] FILEDS = {
            "Front",
            "Back",
            "Image",
            "Data"
    };

    public OcclusionCardModel(Context ct, AnkiDroidHelper ankiDroidHelper){

        context = ct;
        mAnkidroid = ankiDroidHelper;
        try {
            InputStream ips = ct.getResources().getAssets().open(MODEL_FILE);
            byte[] data = new byte[ips.available()];
            ips.read(data);
            String defaultModelStr = new String(data, CODING);
            String[] defaultModelSplitted = defaultModelStr.split(MODEL_SPLITTER);
            if(defaultModelSplitted.length == NUMBER_OF_MODEL_STRING) {
                front[0] = defaultModelSplitted[0];
                back[0] = defaultModelSplitted[1];
                css = defaultModelSplitted[3];
            }
            else{
                ;
            }
            QFMT[0] = front[0];
            AFMT[0] = back[0];
            CSS = css;

        }
        catch(IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private boolean needAddModel(){
        long storedModelId = Settings.getInstance(context).getModelId();
        if(storedModelId > 0){
            return false;
        }else{
            Long mid = mAnkidroid.findModelIdByName(defaultModelName, FILEDS.length);
            if(mid == null){
                return true;
            }else{
                Settings.getInstance(context).setModelId(mid);
                return false;
            }
        }
    }

    private boolean addModel(){
        Long mid = mAnkidroid.getApi().addNewCustomModel(
                defaultModelName,
                FILEDS,
                Cards,
                QFMT,
                AFMT,
                CSS,
                null,
                null
        );
        if(mid != null){
            return true;
        }else{
            return false;
        }
    }
}


