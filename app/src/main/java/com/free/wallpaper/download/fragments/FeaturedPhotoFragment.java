package com.free.wallpaper.download.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.free.wallpaper.download.R;
import com.free.wallpaper.download.activities.DetailActivity;
import com.free.wallpaper.download.adapters.FeatureAdapter;
import com.free.wallpaper.download.adapters.PaginationScrollListener;
import com.free.wallpaper.download.api.UnsplashApiClient;
import com.free.wallpaper.download.api.UnsplashApiService;
import com.free.wallpaper.download.design.GridMarginDecoration;
import com.free.wallpaper.download.helpers.Util;
import com.free.wallpaper.download.interfaces.ClickListener;
import com.free.wallpaper.download.interfaces.PaginationAdapterCallback;
import com.free.wallpaper.download.models.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeaturedPhotoFragment extends Fragment implements PaginationAdapterCallback, ClickListener {

    FeatureAdapter adapter;
    private RecyclerView grid;
    private ProgressBar empty;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    private ArrayList<Photo> featuredPhotos;
    int pageLoadCount = 0;
    // Index from which pagination should start
    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 346;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;
    private boolean isFragmentLoaded = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_main, container, false);
        setupView(rootView);
       /* if (savedInstanceState != null) {
           // relevantPhotos = new ArrayList<>();
            relevantPhotos = savedInstanceState.getParcelableArrayList("saveddata");
            onSavedCalled = true;
        }*/
        btnRetry.setOnClickListener(retry);
        //displayData();
        return rootView;
    }
    View.OnClickListener retry = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideErrorView();
            displayData();
        }
    });

    private void populateGrid() {
        adapter = new FeatureAdapter(getContext(), featuredPhotos, this,this, Glide.with(this));
        grid.setAdapter(adapter);
        empty.setVisibility(View.GONE);
        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
    }

    private void displayData() {
        if (featuredPhotos != null) {
            populateGrid();
        } else {
            UnsplashApiService apiService =
                    UnsplashApiClient.getClient().create(UnsplashApiService.class);
            Call<List<Photo>> call = apiService.getCuratedPhotos(currentPage, Util.ALT_PER_PAGE,Util.ORDER_BY_POPULAR,
                    Util.APPLICATION_ID);
            call.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    if(response.code()==403){
                        showErrorView(new Throwable());
                    }
                    else if (response.code()==200){
                        featuredPhotos =  new ArrayList<>(response.body());
                        populateGrid();
                    }

                }
                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    t.printStackTrace();
                    showErrorView(t);
                }
            });
        }
    }

    public void setupView(View view){
        grid = (RecyclerView) view.findViewById(R.id.image_grid);
        empty = (ProgressBar) view.findViewById(R.id.empty);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
//        final LinearLayoutManager gridLayoutManager =
//                new LinearLayoutManager(getActivity());
       /* gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case PhotoAdapter.ITEM:
                        return 1;
                    case PhotoAdapter.LOADING:
                        return 2;
                    default:
                        return 1;
                }
            }
        });*/
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        grid.setLayoutManager(gridLayoutManager);
        //GridLayoutManager gridLayoutManager = (GridLayoutManager) grid.getLayoutManager();
        final GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /* emulating https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0B6Okdz75tqQsck9lUkgxNVZza1U/style_imagery_integration_scale1.png */
//                switch (position % 6) {
//                    case 5:
//                        return 3;
//                    case 3:
//                        return 2;
//                    default:
//                        return 1;
//                }
                if (position == adapter.getItemCount()-1 && isLoading){
                    return 3;
                }
                else if((position % 6) == 5){
                    return 3;
                }
                else if ((position % 6)==3){
                    return 2;
                }
                return 1;
            }
        });
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grid.setLayoutManager(gridLayoutManager);
        grid.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        grid.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to load the next one
                loadNextPage();
            }
            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void loadNextPage() {
        pageLoadCount +=1;
        UnsplashApiService apiService =
                UnsplashApiClient.getClient().create(UnsplashApiService.class);
        Call<List<Photo>> call = apiService.getCuratedPhotos(currentPage, Util.ALT_PER_PAGE,Util.ORDER_BY_POPULAR,Util.APPLICATION_ID);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.code()==Util.RESPONSE_RATE_LIMIT_EXCEEDED){
                   // showErrorView(new Throwable());
                }
                else if (response.code()==Util.RESPONSE_SUCCESS){
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    ArrayList<Photo> results = new ArrayList<>(response.body());
                    adapter.addAll(results);
                    adapter.notifyDataSetChanged();
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }
    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("saveddata", relevantPhotos);
        //adapter.removeLoadingFooter();
        super.onSaveInstanceState(outState);
    }*/

    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
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
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    @Override
    public void retryPageLoad() {
        loadNextPage();
    }
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putParcelableArrayListExtra("data", featuredPhotos);
        intent.putExtra("pos", position);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && !isFragmentLoaded){
            displayData();
            isFragmentLoaded = true;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
