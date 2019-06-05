package com.stem.chatcake.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stem.chatcake.Fragment.NotificationFragment;
import com.stem.chatcake.Fragment.ProfileFragment;
import com.stem.chatcake.Fragment.RoomsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private RoomsFragment roomsFragment;
    private ProfileFragment profileFragment;
    private NotificationFragment notificationFragment;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.roomsFragment = new RoomsFragment();
        this.profileFragment = new ProfileFragment();
        this.notificationFragment = new NotificationFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return roomsFragment;
            case 1: return profileFragment;
            case 2: return notificationFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Rooms";
            case 1: return "Profile";
            case 2: return "Notification";
            default: return null;
        }
    }

}
