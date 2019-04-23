package com.mmjang.ankillusion.anki;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import com.mmjang.ankillusion.data.IOcclusionExporter;
import com.mmjang.ankillusion.data.OcclusionExportType;
import com.mmjang.ankillusion.data.OcclusionObject;
import com.mmjang.ankillusion.data.OperationResult;
import com.mmjang.ankillusion.data.Settings;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AnkiOcclusionExporter{

    private Activity mActivity;
    private AnkiDroidHelper mAnkidroid;
    private Bitmap mBitmap;
    private Map<Long, String> deckMap;

    public AnkiOcclusionExporter(Activity activity, Bitmap bitmap){
        mActivity = activity;
        mAnkidroid = new AnkiDroidHelper(activity);
        mBitmap = bitmap;
    }

    //obj: List<String>
    public OperationResult getDeckList(){
        try {
            if(deckMap == null) {
                deckMap = mAnkidroid.getApi().getDeckList();
            }
        }catch (Exception e){
            return new OperationResult(false, "Error when read deck list: \n" + e.getLocalizedMessage());
        }
        List<String> deckList = new ArrayList<>();
        for(String d : deckMap.values()){
            deckList.add(d);
        }
        return new OperationResult(true, "Ok", deckList);
    }

    public OperationResult getDeckIdByName(String deckName){
        if(deckMap == null){
            OperationResult or = getDeckList();
            if(!or.isSuccess()){
                return or;
            }
        }
        for(Long id : deckMap.keySet()){
            if (deckMap.get(id).equals(deckName)) {
                return new OperationResult(true, "Ok", id);
            }
        }
        return new OperationResult(false, "No deck named " + deckName +" found!!!");
    }

    public OperationResult export(List<OcclusionObject> occlusionObjectList, Long mDeckId) {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "OCC_" + timeStamp + ".jpg";


        try {
            AnkiDroidHelper ankiDroidHelper = new AnkiDroidHelper(mActivity);
            OcclusionCardModel model = new OcclusionCardModel(mActivity, ankiDroidHelper);
            if (model.needAddModel()) {
                boolean success = model.addModel();
            }
        }catch (Exception e){
            return new OperationResult(false, "Error When add model: \n" + e.getLocalizedMessage());
        }

        FileOutputStream fOut = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory() + "/AnkiDroid/collection.media/");
            if(!root.exists()) {
                root.mkdirs();
            }
            File sdImageMainDirectory = new File(root, imageFileName);
            //outputFileUri = Uri.fromFile(sdImageMainDirectory);
            fOut = new FileOutputStream(sdImageMainDirectory);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            return new OperationResult(false, "Error when write image file: \n" + e.getLocalizedMessage());
        }

        for(OcclusionObject obj : occlusionObjectList){
            try {
                String exportString = obj.toJsonString();
                //exportString = exportString + "";
                mAnkidroid.getApi().addNote(
                        Settings.getInstance(mActivity).getModelId(),
                        mDeckId,
                        new String[] {String.format("<img src='%s'/>", imageFileName), exportString,
                                "",""},
                        null
                );
            } catch (JSONException e) {
                return new OperationResult(false, "Error generating json data: \n" + e.getLocalizedMessage());
            } catch (Exception e){
                return new OperationResult(false,  "Error write cards: \n" + e.getLocalizedMessage());
            }
        }

        return new OperationResult(true, "Ok");
    }
}
