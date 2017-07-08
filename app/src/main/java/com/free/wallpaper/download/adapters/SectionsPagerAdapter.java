package com.free.wallpaper.download.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.free.wallpaper.download.fragments.PlaceholderFragment;
import com.free.wallpaper.download.models.Photo;
import com.free.wallpaper.download.models.PhotoUrl;
import com.free.wallpaper.download.models.UserInfo;

import java.util.ArrayList;

/**
 * Created by Victor on 1/22/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public ArrayList<Photo> photos = new ArrayList<>();
    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Photo> data) {
        super(fm);
        this.photos = data;
        photos.remove(photos.size()-1);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Photo current = photos.get(position);
        UserInfo name = current.getUser();
        PhotoUrl url = current.getUrls();
        String likes = String.valueOf(current.getLikes());

        return PlaceholderFragment.newInstance(position, name.getName(),
                        url.getRegular(),url.getThumb(),likes,url.getFull());

    }

    @Override
    public int getCount() {
        return photos.size();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        Photo current = photos.get(position);
        UserInfo name = current.getUser();
        return name.getName();
    }
}