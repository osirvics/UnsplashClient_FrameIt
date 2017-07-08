package com.free.wallpaper.download.services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.free.wallpaper.download.helpers.Util;

import java.io.IOException;



public class ChangeWallpaperService extends IntentService {
    Bitmap tempbitMap = null;
        public ChangeWallpaperService() {
            // Used to name the worker thread, important only for debugging.
             super("test-service");
            }

    @Override
    public void onCreate() {
        super.onCreate();
       // Log.e("ChangeWallpaperService", "Attempting to load photo");
        //Log.e("ChangeWallpaperService", "Oncreate called");
        // loadImage();
        //getCardColor();
        Toast.makeText(this, "Working apparently", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            //Log.e("ChangeWallpaperService", "Attempting to load photo");
           // loadImage();
        //getCardColor();
    }

    public void getCardColor(){
        Glide.with(getApplicationContext())
                .load("https://source.unsplash.com/random?sig=123")
                .asBitmap()
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                DisplayMetrics metrics = new DisplayMetrics();
                //Log.e("ChangeWallpaperService", "Applying wallpaper");
                WindowManager windowManager = (WindowManager) getApplicationContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(metrics);
                //this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                final int height = metrics.heightPixels;
                final int width = metrics.widthPixels;
                tempbitMap =  Util.resize(resource,width *2 ,height);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    wallpaperManager.setBitmap(tempbitMap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(tempbitMap==null){
                   // Log.e("ChangeWallpaperService", "Temp Bitmap null");
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                //loadImage();
                //Log.e("ChangeWallpaperService", "Loading failed");
                getCardColor();
            }
        });
    }
}
