package com.stem.chatcake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stem.chatcake.R;
import com.stem.chatcake.model.Message;
import com.stem.chatcake.service.LocalStorageService;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

@Setter
public class RoomMessagesAdapter extends ArrayAdapter<Message> {

    private Context context;
    private LocalStorageService localStorageService;
    private ArrayList<Message> messages;

    public RoomMessagesAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects, LocalStorageService localStorageService) {
        super(context, resource, objects);
        this.messages = (ArrayList<Message>) objects;
        this.context = context;
        this.localStorageService = localStorageService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.room_messages_list_item, parent, false);

        LinearLayout container = listItem.findViewById(R.id.messages_list_item_container);
        TextView fromText = listItem.findViewById(R.id.messages_list_item_username);
        TextView messageBox = listItem.findViewById(R.id.messages_list_item_content);

        Message message = messages.get(position);

        fromText.setText(message.getFrom());
        messageBox.setText(message.getContent());


        // style the message
        String username = localStorageService.getUsername();

        if (username.equals(message.getFrom())) {
            messageBox.setBackground(context.getDrawable(R.drawable.message_blue_background));
            messageBox.setTextColor(context.getResources().getColor(android.R.color.white));
            container.setGravity(Gravity.END);
            container.setPadding(50, 0, 0, 0);
        } else {
            messageBox.setBackground(context.getDrawable(R.drawable.message_light_background));
            messageBox.setTextColor(context.getResources().getColor(android.R.color.black));
            container.setGravity(Gravity.START);
            container.setPadding(0, 0, 50, 0);
        }

        return listItem;
    }
}
