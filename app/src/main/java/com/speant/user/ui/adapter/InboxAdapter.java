package com.speant.user.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.SessionManager;
import com.speant.user.Models.ChatList;
import com.speant.user.R;
import com.speant.user.ui.InboxActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {


    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    InboxActivity inboxActivity;

    ArrayList<ChatList> chatMessageArrayList;

    private SessionManager sessionManager;


    public InboxAdapter(InboxActivity inboxActivity, ArrayList<ChatList> chatMessageArrayList) {

        this.inboxActivity = inboxActivity;
        this.chatMessageArrayList = chatMessageArrayList;

        Log.e("Nive", "InboxAdapter:User " + chatMessageArrayList);
        sessionManager = new SessionManager(inboxActivity);


    }


    @NonNull
    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (i == VIEW_TYPE_MESSAGE_SENT) {
            view = inflater.inflate(R.layout.chat_items_socket_sent, parent, false);
        } else if (i == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = inflater.inflate(R.layout.chat_items_socket_receive, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.ViewHolder viewHolder, int position) {

      /*  if (getItemViewType(position) == VIEW_TYPE_MESSAGE_SENT) {

            try {
                Picasso.get().load(Global.IMG_URL + PrefConnect.readString(inboxActivity, Global.IMG_USER, "")).placeholder(R.drawable.default_profile).fit().into(viewHolder.iv_profile);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            try {
                Picasso.get().load(Global.IMG_URL + PrefConnect.readString(inboxActivity, Global.PROVIDER_IMG, "")).placeholder(R.drawable.default_profile).fit().into(viewHolder.iv_profile);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/


        viewHolder.tv_message.setText(chatMessageArrayList.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return chatMessageArrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        ChatList message = (ChatList) chatMessageArrayList.get(position);

        Log.e("Nive ", "getSent_by: " + message.getFrom_id());
        Log.e("Nive ", "getSent_by:UniqueId " + sessionManager.getSocketUniqueId());


        if (message.getFrom_id().equals(sessionManager.getSocketUniqueId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tv_message;

        CircleImageView iv_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }
}
