package com.stem.chatcake.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.stem.chatcake.BR;
import com.stem.chatcake.R;
import com.stem.chatcake.adapter.ViewPagerAdapter;
import com.stem.chatcake.service.StorageService;

public class HomeViewModel extends BaseObservable {

    private String name;
    private Activity host;
    private StorageService storageService;

    public HomeViewModel(Activity host, FragmentManager fragmentManager) {
        this.host = host;
        this.storageService = StorageService.getInstance(host);
        setName(storageService.getUserInfo().getName());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(host, fragmentManager);
        ViewPager viewPager = host.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = host.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.viewModel);
    }
}
