package com.mmjang.ankillusion.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.anki.AnkiOcclusionExporter;
import com.mmjang.ankillusion.data.Constant;
import com.mmjang.ankillusion.data.OcclusionExportType;
import com.mmjang.ankillusion.data.OcclusionObject;
import com.mmjang.ankillusion.data.OcclusionObjectListGenerator;
import com.mmjang.ankillusion.data.OperationResult;
import com.mmjang.ankillusion.data.Settings;
import com.mmjang.ankillusion.utils.Utils;
//import com.tencent.bugly.crashreport.CrashReport;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

import cn.hzw.doodle.DoodleColor;
import cn.hzw.doodle.DoodleOnTouchGestureListener;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleShape;
import cn.hzw.doodle.DoodleTouchDetector;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.IDoodleListener;
import cn.hzw.doodle.MyDoodleOnTouchGestureListener;
import cn.hzw.doodle.core.IDoodle;
import cn.hzw.doodle.occlusion.OcclusionItem;

public class ImageActivity extends AppCompatActivity {
    Settings settings;
    DoodleView doodleView;
    LinearLayout imageContainer;
    CropImageView cropImageView;
    LinearLayout imageCropButtons;
    ImageButton btnRotate;
    ImageButton btnCrop;
    ImageButton btnClose;
    Bitmap originalBitmap;
    private DoodleOnTouchGestureListener touchGestureListener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = Settings.getInstance(this);
        assginViews();
        hideDoodleView(true);
        //Bitmap bitmap = ImageUtils.createBitmapFromPath(Environment.getExternalStorageDirectory() + "/Download/heart.jpg", this);
        //cropImageView.setImageBitmap(bitmap);
        handleIntent();
        setUpListener();

        //CrashReport.testJavaCrash();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.getAction().equals(Intent.ACTION_SEND) && intent.getType().startsWith("image/")){
            try {
                Uri uri = (Uri) intent.getExtras().get(Intent.EXTRA_STREAM);
                cropImageView.setImageUriAsync(uri);
            }
            catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpListener() {
        btnRotate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cropImageView != null){
                            cropImageView.rotateImage(90);
                        }
                    }
                }
        );

        btnCrop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cropImageView != null){
                            //if(cropImageView.)
                            Rect rect = cropImageView.getCropRect();
                            double w = 0, h = 0;
                            if(rect.width() > Constant.MAX_IMAGE_WIDTH){
                                w = Constant.MAX_IMAGE_WIDTH;
                                h = w * ((double) rect.height()/ (double) rect.width());
                                cropImageView.getCroppedImageAsync((int) w, (int) h);
                            }else{
                                cropImageView.getCroppedImageAsync();
                            }
                        }
                    }
                }
        );

        cropImageView.setOnSetImageUriCompleteListener(
                new CropImageView.OnSetImageUriCompleteListener() {
                    @Override
                    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                        //by default, get the whole image;
                        Rect rect = cropImageView.getWholeImageRect();
                        //cropImageView.getCropWindowRect()
//                        Toast.makeText(ImageActivity.this,
//                                "width: " + rect.width() + "height: " + rect.height()
//                                , Toast.LENGTH_SHORT).show();
                        //cropImageView.setCropRect(rect);
                    }
                }
        );

        cropImageView.setOnCropImageCompleteListener(
                new CropImageView.OnCropImageCompleteListener() {
                    @Override
                    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                        setUpDoodleView(result.getBitmap());
                    }
                }
        );
    }

    private void assginViews() {
        //btnDelete = findViewById(R.id.btn_close);
        imageContainer = findViewById(R.id.image_container);
        cropImageView = findViewById(R.id.crop_image_view);
        imageCropButtons = findViewById(R.id.image_crop_buttons);
        btnRotate = findViewById(R.id.btn_rotate);
        btnCrop = findViewById(R.id.btn_crop);
        btnClose = findViewById(R.id.btn_close);
        progressBar = findViewById(R.id.image_progress);
    }

    private void setUpDoodleView(Bitmap bitmap) {
        originalBitmap = bitmap.copy(bitmap.getConfig(), false);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        ankiOcclusionExporter = new AnkiOcclusionExporter(
                                ImageActivity.this,
                                originalBitmap
                        );
                    }
                }
        ).start();

        doodleView = new DoodleView(this, bitmap, new IDoodleListener() {
            @Override
            public void onSaved(IDoodle doodle, Bitmap doodleBitmap, Runnable callback) {
                Toast.makeText(ImageActivity.this, "onSaved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReady(IDoodle doodle) {
                doodle.setSize(30 * doodle.getUnitSize());
            }
        });

        touchGestureListener = new MyDoodleOnTouchGestureListener(doodleView, null);
        DoodleTouchDetector touchDetector = new DoodleTouchDetector(this, touchGestureListener);
        doodleView.enableZoomer(true);
        doodleView.enableOverview(false);
        doodleView.setZoomerScale(Constant.ZOOMER_MULTIPLIER);
        doodleView.setDefaultTouchDetector(touchDetector);
        doodleView.setPen(DoodlePen.BRUSH);
        doodleView.setShape(DoodleShape.FILL_RECT);
        doodleView.setColor(new DoodleColor(Color.parseColor(settings.getOcclusionColor())));

        hideCropView();
        imageContainer.setVisibility(View.VISIBLE);
        imageContainer.addView(doodleView);
    }

    private void showDoodleView(){

    }

    private void clearDoodleView(){
        if(doodleView != null){
            doodleView.clear();
        }
    }

    private void hideCropView(){
        cropImageView.clearImage();
        cropImageView.setVisibility(View.GONE);
        imageCropButtons.setVisibility(View.GONE);
    }

    private void hideDoodleView(boolean clear){
        clearDoodleView();
        if(imageContainer != null){
            imageContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                onEditFrontAndBackText();
                break;
            case R.id.menu_delete:
                if(touchGestureListener != null && touchGestureListener.getSelectedItem()!=null){
                    doodleView.removeItem(touchGestureListener.getSelectedItem());
                }else{
                    Toast.makeText(this, R.string.error_msg_no_selected_occlusion, Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    private void onEditFrontAndBackText() {
        setupNotesCreationDialog();
    }


    private String frontNoteString = "";
    private String backNoteString = "";
    private AnkiOcclusionExporter ankiOcclusionExporter;

    private static final int CARD_CREATION_FINISHED = 50;
    //async
    @SuppressLint("HandlerLeak")
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CARD_CREATION_FINISHED:
                    progressBar.setVisibility(View.GONE);
                    OperationResult or = (OperationResult) msg.obj;
                    if(or.isSuccess()){
                        Toast.makeText(ImageActivity.this, R.string.msg_cards_added, Toast.LENGTH_SHORT).show();
                        if(settings.getAbortAfterSuccess()){
                            ImageActivity.this.finish();
                        }
                    }else{
                        Utils.showMessage(
                                ImageActivity.this,
                                or.getMessage()
                        );
                    }
                    break;
                default:
                    ;
            }
        }
    };


    private void setupNotesCreationDialog() {
        //start card creation
        if(doodleView == null || originalBitmap == null){
            Utils.showMessage(ImageActivity.this, getString(R.string.error_msg_crop_the_image_first));
            return ;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ImageActivity.this);
        LayoutInflater inflater = ImageActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.msg_add_cards_to_ankidroid);
        //set views
        final Spinner deckSpinner = dialogView.findViewById(R.id.deck_spinner);
        final Spinner modeSpinner = dialogView.findViewById(R.id.mode_spinner);
        final EditText editTextFrontNode = dialogView.findViewById(R.id.edittext_front_note);
        final EditText editTExtBackNote = dialogView.findViewById(R.id.edittext_back_note);
        //写入已存笔记
        editTextFrontNode.setText(frontNoteString);
        editTExtBackNote.setText(backNoteString);
        //init exporter
        if(ankiOcclusionExporter == null){
            ankiOcclusionExporter = new AnkiOcclusionExporter(
                    ImageActivity.this,
                    originalBitmap
            );
        }
        //init deck spinner
        OperationResult deckOpResult = ankiOcclusionExporter.getDeckList();
        if(!deckOpResult.isSuccess()){
            Utils.showMessage(ImageActivity.this, deckOpResult.getMessage());
            return;
        }
        final List<String> deckList = (List<String>) deckOpResult.getResult();
        final String[] deckArr = new String[deckList.size()];
        for (int i = 0; i < deckList.size(); i++) {
            deckArr[i] = deckList.get(i);
        }
        ArrayAdapter<String> deckSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, deckArr);
        deckSpinner.setAdapter(deckSpinnerAdapter);
        deckSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String storedDeckName = Settings.getInstance(ImageActivity.this).getDeckName();
        if(!Settings.getInstance(ImageActivity.this).getDeckName().isEmpty()){
            for(int i = 0; i < deckList.size(); i ++){
                if(deckList.get(i).equals(storedDeckName)){
                    deckSpinner.setSelection(i);
                }
            }
        }
        //init mode spinner
        modeSpinner.setSelection(Settings.getInstance(ImageActivity.this).getCreationMode());
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                progressBar.setVisibility(View.VISIBLE);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<OcclusionItem> occlusionItemList = doodleView.getOcclusionList();
                        int height = doodleView.getDoodleBitmap().getHeight();
                        int width = doodleView.getDoodleBitmap().getWidth();
                        String deckName = deckList.get(deckSpinner.getSelectedItemPosition());
                        //save deckname
                        Settings.getInstance(ImageActivity.this).setDeckName(deckName);
                        //save mode
                        Settings.getInstance(ImageActivity.this).setCreationMode(modeSpinner.getSelectedItemPosition());
                        int mode = modeSpinner.getSelectedItemPosition();
                        OcclusionExportType type = null;
                        if(mode == 0){
                            type = OcclusionExportType.HIDE_ALL_REVEAL_ALL;
                        }
                        if(mode == 1){
                            type = OcclusionExportType.HIDE_ONE_REVEAL_ONE;
                        }
                        if(mode == 2){
                            type = OcclusionExportType.HIDE_ALL_REVEAL_ONE;
                        }

                        List<OcclusionObject> occlusionObjectList = OcclusionObjectListGenerator.gen(
                                1,
                                "place_holder.jpg",
                                width,
                                height,
                                occlusionItemList,
                                type
                        );

                        OperationResult orDeckId = ankiOcclusionExporter.getDeckIdByName(deckName);
                        //failed to get deck id
                        if(!orDeckId.isSuccess()){
                            Message message = mHandler.obtainMessage();
                            message.obj = orDeckId;
                            message.what = CARD_CREATION_FINISHED;
                            mHandler.sendMessage(message);
                            return ;
                        }

                        OperationResult orExport = ankiOcclusionExporter.export(
                                occlusionObjectList,
                                (Long) orDeckId.getResult(),
                                editTextFrontNode.getText().toString(),
                                editTExtBackNote.getText().toString()
                        );

                        Message message = mHandler.obtainMessage();
                        message.obj = orExport;
                        message.what = CARD_CREATION_FINISHED;
                        mHandler.sendMessage(message);
                    }
                });
                thread.start();
            }
        });
        AlertDialog b = dialogBuilder.create();

        //退出对话框时保存笔记
        b.setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        frontNoteString = editTextFrontNode.getText().toString();
                        backNoteString = editTExtBackNote.getText().toString();
                    }
                }
        );
        b.show();
    }
}
