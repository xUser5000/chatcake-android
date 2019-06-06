package com.stem.chatcake.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.stem.chatcake.R;
import com.stem.chatcake.adapter.ViewPagerAdapter;
import com.stem.chatcake.service.SocketService;
import com.stem.chatcake.service.StorageService;

import lombok.Builder;

@Builder
public class HomeViewModel {

    // dependencies
    private Activity host;
    private StorageService storageService;
    private SocketService socketService;
    private FragmentManager fragmentManager;

    // state
    public final ObservableField<String> name = new ObservableField<>();
    private boolean connected;

    public void init () {

        connect();

        name.set(storageService.getUserInfo().getName());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(host, fragmentManager);
        ViewPager viewPager = host.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabs = host.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void connect () {
        // socket connection
        socketService.connect();
        // authentication
        String token = storageService.getToken();
        socketService.authenticate(host, token, new Runnable() {
            @Override
            public void run() {
                Toast.makeText(host, "Socket Connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
