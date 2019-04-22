package com.mmjang.ankillusion.data;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;

import com.mmjang.ankillusion.MyApplication;
import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.utils.Utils;

public class Constant {
    public static final String DEFAULT_OCCLUSION_COLOR = Utils.color2Hex(
            MyApplication.getContext().getResources().getColor(R.color.colorPrimaryDark));//"#fce77d";
    public static final String DEFAULT_OCCLUSION_COLOR_HIGHLIGHT = Utils.color2Hex(
            MyApplication.getContext().getResources().getColor(R.color.colorAccent)); //"#f96167";
    public static final String ANKI_PACKAGE_NAME = "com.ichi2.anki";
    public static final String[] PERMISSION_FOR_EXPORT = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            com.ichi2.anki.api.AddContentApi.READ_WRITE_PERMISSION
    };
    public static final int MAX_IMAGE_WIDTH = 1080;
    public static final String OCCLUSION_MODEL_NAME = "ankillusion_note_type";
}