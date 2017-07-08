package com.free.wallpaper.download.api;

import com.free.wallpaper.download.models.Collection;
import com.free.wallpaper.download.models.Photo;
import com.free.wallpaper.download.models.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Victor on 1/11/2017.
 */

public interface UnsplashApiService {
    @GET("photos/curated")
    Call<List<Photo>> getCuratedPhotos(@Query("page") int page,
                                       @Query("per_page") int per_page,
                                       @Query("order_by") String order_by,
                                       @Query("client_id") String apiKey);

    //@Headers("Authorization": "Client-ID " = Util.APPLICATION_ID)
    @GET("photos")
    Call<List<Photo>> getPhotos(@Header("Authorization") String token,
                                @Query("page") int page,
                                @Query("per_page") int per_page,
                                @Query("order_by") String order_by
                              );

    @GET("collections")
    Call<List<Collection>> getAllCollections(@Query("page") int page,
                                             @Query("per_page") int per_page);

    @GET("collections/curated")
    Call<List<Collection>> getCuratedCollections(@Query("page") int page,
                                                 @Query("per_page") int per_page);

    @GET("collections/featured")
    Call<List<Collection>> getFeaturedCollections(@Query("page") int page,
                                                  @Query("per_page") int per_page,
                                                  @Query("client_id") String apiKey);

    @GET("collections/{id}")
    Call<Collection> getACollection(@Path("id") String id);

    @GET("collections/curated/{id}")
    Call<Collection> getACuratedCollection(@Path("id") String id);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getCollectionList(@Path("id") String id,
                                 @Query("page") int page,
                                 @Query("per_page") int per_page,
                                 @Query("client_id") String apiKey);

    @GET("search/photos")
    Call<SearchResult> getSearchPhotos(@Header("Authorization") String token,
                                    @Query("query") String query,
                                    @Query("page") int page,
                                    @Query("per_page") int per_page);

}


