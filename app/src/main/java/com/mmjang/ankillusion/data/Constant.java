package com.mmjang.ankillusion.data;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;

public class Constant {
    public static final String DEFAULT_OCCLUSION_COLOR = "#fce77d";
    public static final String DEFAULT_OCCLUSION_COLOR_HIGHLIGHT = "#f96167";
    public static final String ANKI_PACKAGE_NAME = "com.ichi2.anki";
    public static final String[] PERMISSION_FOR_EXPORT = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            com.ichi2.anki.api.AddContentApi.READ_WRITE_PERMISSION
    };
}