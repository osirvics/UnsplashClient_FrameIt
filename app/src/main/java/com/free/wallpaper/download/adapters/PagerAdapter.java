package com.free.wallpaper.download.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.free.wallpaper.download.fragments.CollectionsFragment;
import com.free.wallpaper.download.fragments.FeaturedPhotoFragment;
import com.free.wallpaper.download.fragments.NewPhotosFragment;

/**
 * Created by Victor on 1/23/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;


    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewPhotosFragment tab1 = new NewPhotosFragment();
                return tab1;
            case 1:
                FeaturedPhotoFragment tab2 = new FeaturedPhotoFragment();
                return tab2;
            case 2:
                CollectionsFragment tab3 = new CollectionsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return  mNumOfTabs;
    }
}
