package com.free.wallpaper.download.interfaces;

/**
 * Listens for item clicks
 */

public interface  ClickListener {
    void onItemClicked(int position);
    boolean onItemLongClicked(int position);
}
