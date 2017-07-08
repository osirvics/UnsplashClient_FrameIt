package com.free.wallpaper.download.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.free.wallpaper.download.R;

import de.psdev.licensesdialog.LicensesDialog;


public class AboutActivity extends AppCompatActivity {
    String packageName = "com.free.wallpaper.download";
    LinearLayout view;
    LinearLayout bugs;
    //Group one
    TextView cardOneHeading, cardOneVersion,cardOneVersion2, cardOneLicenses;
    LinearLayout first;
    AppCompatImageView version, licences, report_bugs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if(toolbar!=null)
//            toolbar.setTitle(R.string.about);
//        setSupportActionBar(toolbar);
//        if(getSupportActionBar()!=null)
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_licenses, null);


        setCardOne();
       // applyColorFilter();

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMultipleClick(view);
            }
        });

        bugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:victor46539@gmail.com"));
                intent.putExtra(Intent.EXTRA_EMAIL, "addresses");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_via)));
                }
            }
        });
    }

    public void setCardOne() {
        bugs = (LinearLayout) findViewById(R.id.layout_bug);
        bugs = (LinearLayout) findViewById(R.id.layout_bug);
        cardOneHeading = (TextView) findViewById(R.id.group_one_heading);
        cardOneVersion = (TextView) findViewById(R.id.version_text_one);
        cardOneVersion2 = (TextView) findViewById(R.id.version_text_two);
        cardOneLicenses = (TextView) findViewById(R.id.license_text);
        licences = (AppCompatImageView) findViewById(R.id.license_icon);
        version = (AppCompatImageView) findViewById(R.id.version_icon);
        first = (LinearLayout) findViewById(R.id.first);
        cardOneVersion2.setText(getCurrentVersionName(AboutActivity.this));
        licences.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimaryDark));
    }










//    private void applyColorFilter(){
//
//            licences.setColorFilter(ContextCompat.getColor(this,R.color.md_grey_600));
//            version.setColorFilter(ContextCompat.getColor(this,R.color.md_grey_600));
////            communityView.setColorFilter(ContextCompat.getColor(this,R.color.md_grey_600));
////            rate_app.setColorFilter(ContextCompat.getColor(this,R.color.md_grey_600));
////            remove_ads.setColorFilter(ContextCompat.getColor(this,R.color.md_grey_600));
//    }

    private void onMultipleClick(final View view) {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.licenses)
                .setTitle("Licenses")
                .build()
                .show();
    }

    private static String getCurrentVersionName(@NonNull Context paramContext)
    {
        try
        {
            return   paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;

        }
        catch (PackageManager.NameNotFoundException ee)
        {
            ee.printStackTrace();
        }
        return "0.0.0";
    }




}
