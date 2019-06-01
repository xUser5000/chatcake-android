package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomsViewModel extends BaseObservable {

    // dependencies
    private Context context;
    private View parent;
    private HttpService httpService;
    private StorageService storageService;

    // state
    private boolean loading = false;

    public RoomsViewModel () {

    }

}
