package com.example.bingewatchers;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    FirebaseAuth mAuth;
    int MAX_LINES = 5;
    private List<Message> Chats = null;
    private ArrayList<Message> arraylist;

    public ChatListAdapter(Context context, ArrayList<Message> Chats) {
        mContext = context;
        this.Chats = Chats;
        inflater = LayoutInflater.from(mContext);
        mAuth = FirebaseAuth.getInstance();
        this.arraylist = new ArrayList<Message>();
        this.arraylist.addAll(Chats);
    }

    @Override
    public int getCount() {
        return Chats.size();
    }

    @Override
    public Message getItem(int position) {
        return Chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            if (Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail())) {
                if (Chats.get(position).getType().equals("message")) {
                    view = inflater.inflate(R.layout.chat_list_own, null);
                    holder.message = view.findViewById(R.id.message);
                    holder.time = (TextView) view.findViewById(R.id.time);

                    holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message.setText(Chats.get(position).getMessage());

                } else {
                    view = inflater.inflate(R.layout.chat_list_suggestion, null);
                    holder.poster = view.findViewById(R.id.picture_text);
                    holder.message = view.findViewById(R.id.message);
                    holder.time = (TextView) view.findViewById(R.id.time);

                    holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message.setText(Chats.get(position).getMessage());
                    Glide.with(mContext).asBitmap()
                            .load(Chats.get(position).getUrl())
                            .into(holder.poster);
                }
            } else {
                if (Chats.get(position).getType().equals("message")) {
                    view = inflater.inflate(R.layout.chat_list_other, null);
                    holder.message = view.findViewById(R.id.message);
                    holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);
                    holder.time = (TextView) view.findViewById(R.id.time);

                    holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message.setText(Chats.get(position).getMessage());
                    holder.senderEmail.setText(Chats.get(position).getName());
                } else {
                    view = inflater.inflate(R.layout.chat_list_suggestion_other, null);
                    holder.poster = view.findViewById(R.id.picture_text);
                    holder.message = view.findViewById(R.id.message);
                    holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);
                    holder.time = (TextView) view.findViewById(R.id.time);

                    holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message.setText(Chats.get(position).getMessage());
                    holder.senderEmail.setText(Chats.get(position).getName());

                    Glide.with(mContext).asBitmap()
                            .load(Chats.get(position).getUrl())
                            .into(holder.poster);
                }
            }
            holder.message.setShowingLine(6);

            holder.message.addShowMoreText("Show More");
            holder.message.addShowLessText("Show Less");
//        holder.message.setTexM
        }


        return view;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    public class ViewHolder {


        TextView senderEmail;
        //TextView message;
        ShowMoreTextView message;
        ImageView poster;
        TextView time;

    }
}