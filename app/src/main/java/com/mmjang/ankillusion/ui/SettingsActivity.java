package com.mmjang.ankillusion.ui;

import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.data.Settings;
import com.mmjang.ankillusion.utils.Utils;

import top.defaults.colorpicker.ColorPickerPopup;

public class SettingsActivity extends AppCompatActivity {

    View btnRectAngleColor;
    View btnRectAngleHighlightColor;
    Switch switchAbortAfterCardCreation;
    Settings mySettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySettings = Settings.getInstance(this);
        btnRectAngleColor = findViewById(R.id.btn_select_rectangle_color);
        btnRectAngleHighlightColor = findViewById(R.id.btn_select_rectangle_highlight_color);
        btnRectAngleColor.setBackgroundColor(Color.parseColor(mySettings.getOcclusionColor()));
        btnRectAngleHighlightColor.setBackgroundColor(Color.parseColor(mySettings.getOcclusionColorHighlight()));
        switchAbortAfterCardCreation = findViewById(R.id.switch_exit_after_card_creation);

        btnRectAngleColor.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new ColorPickerPopup.Builder(SettingsActivity.this)
                                .initialColor(Color.parseColor(mySettings.getOcclusionColor())) // Set initial color
                                .enableBrightness(true) // Enable brightness slider or not
                                .enableAlpha(false) // Enable alpha slider or not
                                .okTitle(getString(R.string.title_choose))
                                .cancelTitle(getString(R.string.title_cancel))
                                .showIndicator(true)
                                .showValue(true)
                                .build()
                                .show(v, new ColorPickerPopup.ColorPickerObserver() {
                                    @Override
                                    public void onColorPicked(int color) {
                                        v.setBackgroundColor(color);
                                        mySettings.setOcclusionColor(Utils.color2Hex(color));
                                    }
                                });
                    }
                }
        );

        btnRectAngleHighlightColor.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new ColorPickerPopup.Builder(SettingsActivity.this)
                                .initialColor(Color.parseColor(mySettings.getOcclusionColorHighlight())) // Set initial color
                                .enableBrightness(true) // Enable brightness slider or not
                                .enableAlpha(false) // Enable alpha slider or not
                                .okTitle(getString(R.string.title_choose))
                                .cancelTitle(getString(R.string.title_cancel))
                                .showIndicator(true)
                                .showValue(true)
                                .build()
                                .show(v, new ColorPickerPopup.ColorPickerObserver() {
                                    @Override
                                    public void onColorPicked(int color) {
                                        v.setBackgroundColor(color);
                                        mySettings.setOcclusionColorHighlight(Utils.color2Hex(color));
                                    }
                                });
                    }
                }
        );

        switchAbortAfterCardCreation.setChecked(
                mySettings.getAbortAfterSuccess()
        );

        switchAbortAfterCardCreation.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mySettings.setAbortAfterSuccess(isChecked);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
