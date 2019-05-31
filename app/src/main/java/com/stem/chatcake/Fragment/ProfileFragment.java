package com.stem.chatcake.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stem.chatcake.R;
import com.stem.chatcake.service.StorageService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent =  inflater.inflate(R.layout.fargment_profile, container, false);

        TextView usernameText = parent.findViewById(R.id.profile_username_text);
        StorageService storageService = StorageService.getInstance(getContext());

        String username = "Username: " + storageService.getUsername();
        usernameText.setText(username);

        return parent;
    }

}
