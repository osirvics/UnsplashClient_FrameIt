package com.free.wallpaper.download.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.free.wallpaper.download.R;
import com.free.wallpaper.download.adapters.PagerAdapter;
import com.free.wallpaper.download.helpers.Util;
import com.free.wallpaper.download.helpers.WallpaperFirebaseScheduler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.home_fragment));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        //Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
       //12 WallpaperScheduler alarm = new WallpaperScheduler();
        //alarm.setAlarm(this);
       // Log.e("MainActivity", "set alarm called");
        initTabedViewpager();
        setupDrawer(toolbar);
        WallpaperFirebaseScheduler.scheduleChargingReminder(this);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void setupDrawer(Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void initTabedViewpager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.list_of_new_photos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.list_of_featured_photos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.list_of_photo_collections));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#448aff"));
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
       // tabLayout.setBackgroundColor(primaryColor);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        assert viewPager != null;
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                //Log.e("currentTab", "selected");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        requestNewInterstitial();
                    }
                });
                //Log.e("currentTab", "Unselected");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        //viewPager.setBackgroundColor(primaryColor);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.action_search:
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent);
                    }
                },80);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent about = new Intent(MainActivity.this, PhotoOfDayActivity.class);
                    startActivity(about);
                }
            },100);


        } else if (id == R.id.nav_gallery) {
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent about = new Intent(MainActivity.this, CatActivity.class);
                    startActivity(about);
                }
            },100);
            /*Glide.get(this).clearMemory();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(MainActivity.this).clearDiskCache();
                }
            }).start();

            Toast.makeText(this, "All cached cleared",Toast.LENGTH_SHORT).show();*/

        } else if (id == R.id.nav_slideshow) {
            Util.openPlayStore(this,Util.packageName);

        }/* else if (id == R.id.nav_manage) {

        }*/ else if (id == R.id.nav_share) {
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent about = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(about);
                }
            },100);

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:victor46539@gmail.com"));  // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "addresses");
            intent.putExtra(Intent.EXTRA_SUBJECT, "FrameIt");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_settings) {
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent about = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(about);
                }
            },100);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
