package com.mmjang.ankillusion.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmjang.ankillusion.BuildConfig;
import com.mmjang.ankillusion.R;

public class AboutActivity extends AppCompatActivity {

    TextView mTextViewAboutInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setAppNameAndVersion();

        mTextViewAboutInformation = findViewById(R.id.textview_about_information);
        mTextViewAboutInformation.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView btnBuymeacoffee = findViewById(R.id.btn_buymeacoffee);
        ImageView btnAlipay = findViewById(R.id.btn_alipay);

        btnBuymeacoffee.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://www.buymeacoffee.com/w05dHCN";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
        );

        btnAlipay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String payUrl = "HTTPS://QR.ALIPAY.COM/FKX011406PTCIHXZJPW7A1";
                        //String payUrl = "HTTPS://QR.ALIPAY.COM/A6X00376AFOZWZUHWTDNDF4"; //any
                        intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            intent.setData(Uri.parse(payUrl.toLowerCase()));
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    private TextView mAppNameAndVersion;
    private void setAppNameAndVersion(){
        if(mAppNameAndVersion == null){
            mAppNameAndVersion = findViewById(R.id.textview_app_name_and_version);
        }
        mAppNameAndVersion.setText(
                getText(R.string.app_name) + " ver: " + BuildConfig.VERSION_NAME);
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
