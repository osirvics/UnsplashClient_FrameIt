package com.free.wallpaper.download.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.free.wallpaper.download.R;
import com.free.wallpaper.download.interfaces.ClickListener;
import com.free.wallpaper.download.interfaces.PaginationAdapterCallback;
import com.free.wallpaper.download.models.Photo;
import com.free.wallpaper.download.models.PhotoUrl;
import com.free.wallpaper.download.models.UserInfo;

import java.util.ArrayList;

/**
 * Created by Victor on 1/14/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Photo> photos;
    public ArrayList<PhotoUrl> photosUrl;
    private Context context;
    public static final int ITEM = 0;
    public static final int LOADING = 1;
    public ClickListener clickListener;
    private final int requestedPhotoWidth;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private PaginationAdapterCallback mCallback;
    private String errorMsg;
    private final RequestManager glide;

    public PhotoAdapter(@NonNull Context context, @NonNull ArrayList<Photo> photos,
                        PaginationAdapterCallback adapterCallback, ClickListener clickListener, RequestManager mGlide) {
        this.photos = photos;
        this.context = context;
        this.mCallback = adapterCallback;
        this.clickListener = clickListener;
        this.glide = mGlide;
        requestedPhotoWidth = context.getResources().getDisplayMetrics().widthPixels;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case ITEM:
                View view = layoutInflater
                        .inflate(R.layout.photo_item, parent, false);
                viewHolder = new PhotoHolder(view, clickListener);
                break;
            case LOADING:
                View view2 = layoutInflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(view2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                final PhotoHolder fholder = (PhotoHolder) holder;
                //Log.e("Adapter", "size of items in adpter: " + photos.size());
                Photo data = photos.get(position);
                PhotoUrl url = data.getUrls();
                UserInfo user = data.getUser();
                glide
                        .load(url.getRegular())
                        .placeholder(R.color.author_background)
                        .override(480,400)
                        .fitCenter()
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .thumbnail(glide
//                                .load(url.getThumb())
//                                .override(100, 100)
//                                .transform(new BlurTransformation(context)))
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(fholder.image);

                /*try {
                    fholder.author.setText(user.getName());
                    Glide.with(context)
                            .load(url.getRegular())
                            .placeholder(R.color.author_background)
                            .centerCrop()
                            .fitCenter()
                            .crossFade()
                            .thumbnail(Glide.with(context)
                                    .load(url.getThumb())
                                    .override(50, 50)
                                    .transform(new BlurTransformation(context)))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.override(ImageSize.NORMAL[0], ImageSize.NORMAL[1])
                            .into(fholder.image);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Item of position crash ", "Is: " + position);
                }

*/
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.progressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.progressBar.setVisibility(View.VISIBLE);
                }
                /*LoadingVH loadingViewHolder = (LoadingVH) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);*/
//                Do nothing
                break;
        }

    }
    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        //super.onViewRecycled(holder);
        if(holder instanceof PhotoHolder ){
            final PhotoHolder fholder = (PhotoHolder) holder;
            Glide.clear(fholder.image);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Photo mc) {
        photos.add(mc);
        notifyItemInserted(photos.size() - 1);
    }

    public void addAll(ArrayList<Photo> mcList) {
        for (Photo mc : mcList) {
            add(mc);
        }
    }

    public void remove(Photo city) {
        int position = photos.indexOf(city);
        if (position > -1) {
            photos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Photo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = photos.size() - 1;
        Photo item = getItem(position);
        if (item != null) {
            photos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Photo getItem(int position) {
        return photos.get(position);
    }


    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(photos.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView author;
        public ImageView image;
        public final View mView;
        public ClickListener listener;

        public PhotoHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            mView = itemView;
            image = (ImageView) itemView.findViewById(R.id.photo);
            author = (TextView) itemView.findViewById(R.id.author);
            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClicked(getLayoutPosition());
            }
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ProgressBar progressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loadmore_retry:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }
}
