package com.free.wallpaper.download.fragments;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.free.wallpaper.download.R;
import com.free.wallpaper.download.design.BlurTransformation;
import com.free.wallpaper.download.helpers.Util;
import com.free.wallpaper.download.interfaces.LoadListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static android.R.attr.thumb;

/**
 * A placeholder fragment containing a simple view.
 */
public  class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    @Override
    public void onStart() {
        super.onStart();

    }

    String name, url, thum,numLikes,full;
    int pos;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_IMG_TITLE = "image_title";
    private static final String ARG_IMG_URL = "image_url";
    private static final String ARG_IMG_THUMBNAIL = "image_thumbnail";
    private static final String ARG_IMG_NO_LIKES = "image_likes";
    private static final String ARG_IMG_FULL = "image_full";
    CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    LoadListener load;
    private ImageView imageView;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError,textErrorHeader;
    boolean isShowImage = false;
    boolean isApplyWallpaper = false;


    public PlaceholderFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.pos = args.getInt(ARG_SECTION_NUMBER);
        this.name = args.getString(ARG_IMG_TITLE);
        this.url = args.getString(ARG_IMG_URL);
        this.thum = args.getString(ARG_IMG_THUMBNAIL);
        this.numLikes = args.getString(ARG_IMG_NO_LIKES);
        this.full =args.getString(ARG_IMG_FULL);
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, String name, String url,
                                                  String thum, String likes, String full) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_IMG_TITLE, name);
        args.putString(ARG_IMG_URL, url);
        args.putString(ARG_IMG_THUMBNAIL,thum);
        args.putString(ARG_IMG_NO_LIKES,likes);
        args.putString(ARG_IMG_FULL,full);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id
                .main_content);
        imageView = (ImageView) rootView.findViewById(R.id.detail_image);
        TextView author = (TextView) rootView.findViewById(R.id.author);
        TextView num_likes = (TextView) rootView.findViewById(R.id.likes);
        progressBar = (ProgressBar) rootView.findViewById(R.id.empty);
        errorLayout = (LinearLayout)rootView.findViewById(R.id.error_layout);
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);
        txtError = (TextView) rootView.findViewById(R.id.error_txt_cause);
        textErrorHeader = (TextView) rootView.findViewById(R.id .error_header);
        load = (LoadListener)getActivity();
        author.setText(getString(R.string.photo_by,name));
        Util.setTypeface(getActivity(), author);
        num_likes.setText(numLikes);
        Util.setTypeface(getActivity(),num_likes);
        progressBar.setVisibility(View.VISIBLE);
        btnRetry.setOnClickListener(retry);


        displayImage();
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                applyWallpaper(getActivity());

            }
        });


        return rootView;
    }

    View.OnClickListener retry = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideErrorView();
            if(isShowImage){
                displayImage();
            }
            else if(isApplyWallpaper){
                applyWallpaper(getActivity());
            }

        }
    });

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            load.loadComplete();
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void displayImage(){
        Glide.with(this)
                .load(url)
                .placeholder(R.color.author_background)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(Glide.with(this)
                        .load(thumb)
                        .override(100, 100)
                        .transform(new BlurTransformation(getActivity())))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        isShowImage = true;
                        isApplyWallpaper = false;
                        showErrorView(e);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                       // isShowImage = false;
                        return false;
                    }
                })
                // cache both original & resized image
                //.centerCrop()
                .fitCenter()
                .into(imageView);
    }

    private void applyWallpaper(final Context context){
        load.loadStarted();
        Glide.with(getActivity())
                .load(full)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        isShowImage = false;
                        isApplyWallpaper = true;
                        showErrorView(e);
//                        Log.e("Load faiiled", "called");
//                        Snackbar snackbar = Snackbar
//                                .make(coordinatorLayout,fetchErrorMessage(e) , Snackbar.LENGTH_INDEFINITE)
//                                .setAction(getString(R.string.retry), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        progressBar.setVisibility(View.VISIBLE);
//                                    applyWallpaper();
//                                    }
//                                });
//                        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(),R.color
//                                .colorAccent));
                        super.onLoadFailed(e, errorDrawable);
                    }

                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        final int height = metrics.heightPixels;
                        final int width = metrics.widthPixels;
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());
                                //wallpaperManager.setWallpaperOffsetSteps(1, 1);
                                Bitmap tempbitMap = Util.resize(resource,width *2 ,height);
                                //Bitmap myBitmap = Bitmap.createScaledBitmap(tempbitMap, wallpaperManager.getDesiredMinimumWidth() , wallpaperManager.getDesiredMinimumHeight(), true);
                                //wallpaperManager.suggestDesiredDimensions(wallpaperManager.getDesiredMinimumWidth(), wallpaperManager.getDesiredMinimumHeight());

                                //wallpaperManager.suggestDesiredDimensions(width, height);
                                try {
                                    wallpaperManager.setBitmap(tempbitMap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                load.loadComplete();
                                if(isAdded())
                                Toast.makeText(context, getString(R.string.success),Toast.LENGTH_SHORT).show();
                            }
                        }.execute();
                    }
                });
    }



    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }
        if(isApplyWallpaper){
            textErrorHeader.setText(getString(R.string.error_header));
            textErrorHeader.setTextColor(Color.WHITE);
            txtError.setTextColor(Color.WHITE);
            errorLayout.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.black_overlay));
        }

        return errorMsg;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}