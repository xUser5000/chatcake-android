package com.stem.chatcake.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stem.chatcake.R;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRoomDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText roomNameText;
    private Button createButton;
    private HttpService httpService;
    private StorageService storageService;

    public CreateRoomDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.fragment_create_room_dialog, null);
        roomNameText = parent.findViewById(R.id.create_room_name);
        createButton = parent.findViewById(R.id.create_room_button);
        createButton.setOnClickListener(this);
        httpService = HttpService.getInstance();
        storageService = StorageService.getInstance(getContext());
        builder.setView(parent);
        builder.setTitle("Create new room");
        return builder.create();
    }

    public void createRoom () {
        String token = storageService.getToken();
        final String roomName = roomNameText.getText().toString().trim();

        if (roomName.equals("")) {
            Toast.makeText(getContext(), "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Room> call = httpService.getApi().createRoom(token, roomName);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (!response.isSuccessful()) {
                    httpService.showErrors(getContext(), response);
                    return;
                }

                Toast.makeText(getContext(), roomName + " room Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("roomName", roomName);
                intent.putExtra("roomId", response.body().getId());
                getTargetFragment().onActivityResult(0, 0, intent);
                dismiss();
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_room_button) {
            createRoom();
        }
    }
}
