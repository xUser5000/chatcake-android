package com.stem.chatcake.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stem.chatcake.R;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomInfoDialogFragment extends DialogFragment {

    private HttpService httpService;

    public RoomInfoDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.fragment_create_room_dialog, null);

        httpService = HttpService.getInstance();

        builder.setView(parent);
        builder.setTitle("Create new room");
        return builder.create();
    }

}
