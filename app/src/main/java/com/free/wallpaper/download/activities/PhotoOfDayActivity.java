package com.free.wallpaper.download.activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.free.wallpaper.download.R;
import com.free.wallpaper.download.design.BlurTransformation;
import com.free.wallpaper.download.helpers.Util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class PhotoOfDayActivity extends AppCompatActivity  {
    private ProgressBar empty;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    ImageView imageView;
    Bitmap tempbitMap = null;
    FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_of_day);
        setupView();
        loadImage();
        fab.setOnClickListener(fabClick);
        btnRetry.setOnClickListener(retry);
        Util.setTranslucentStatusBar(getWindow());


       /* if (savedInstanceState != null) {
           // relevantPhotos = new ArrayList<>();
            relevantPhotos = savedInstanceState.getParcelableArrayList("saveddata");
            onSavedCalled = true;
        }*/
        //btnRetry.setOnClickListener(retry);
    }

    View.OnClickListener retry = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideErrorView();
            loadImage();
        }
    });

    View.OnClickListener fabClick =(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(PhotoOfDayActivity.this);
                    try {
                        wallpaperManager.setBitmap(tempbitMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    empty.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Wallpaper set successfully",Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
    });




    public void setupView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        empty = (ProgressBar) findViewById(R.id.photo_loading);
        imageView = (ImageView) findViewById(R.id.photo);
        fab.setVisibility(View.INVISIBLE);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);
    }

    private void loadImage(){
        Glide.with(this)
                .load("https://source.unsplash.com/featured/daily")
                .asBitmap()
                .placeholder(R.color.author_background)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .thumbnail(Glide.with(this)
                        .load("https://source.unsplash.com/featured/daily")
                        .asBitmap()
                        .override(50, 50)
                        .transform(new BlurTransformation(this)))
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        showErrorView(e);

               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        final int height = metrics.heightPixels;
                        final int width = metrics.widthPixels;
                        tempbitMap =  Util.resize(resource,width *2 ,height);
                        empty.setVisibility(View.GONE);
                        fab.show();
                        return false;
                    }
                })
                .fitCenter()
                .into(imageView);
    }


    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("saveddata", relevantPhotos);
        //adapter.removeLoadingFooter();
        super.onSaveInstanceState(outState);
    }*/

    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }
    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }
        return errorMsg;
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
//            } else if (item.getItemId() == R.id.about) {
//               // AboutDialog.show(this);2
//                return true;
//            }

        }
        return super.onOptionsItemSelected(item);
    }
}
