package com.example.bingewatchers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    FirebaseAuth mAuth;
    private List<Message> Chats = null;
    private ArrayList<Message> arraylist;
    private int i = 0;

    public ChatListAdapter(Context context, ArrayList<Message> Chats) {
        mContext = context;
        this.Chats = Chats;
        inflater = LayoutInflater.from(mContext);
        mAuth = FirebaseAuth.getInstance();
        this.arraylist = new ArrayList<Message>();
        this.arraylist.addAll(Chats);
    }

    public ChatListAdapter(Context context, java.util.ArrayList<Message> Chats, int i2) {
        i = 1;
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
            if (Chats.get(position).getType().equals("Suggestion")) {

//                view = inflater.inflate(R.layout.chat_list_suggestion, null);
//                view = inflater.inflate(R.layout.chat_list_suggestion_other, null);

                System.out.println(
                        "        COMPARE                  " + Chats.get(position).getSenderEmail() + "==" + mAuth.getCurrentUser().getEmail().toString());

                if (Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().toString().trim())) {
                    view = inflater.inflate(R.layout.chat_list_suggestion, null);

//                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  IMAGE" + Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().toString().trim()));
                } else {
                    view = inflater.inflate(R.layout.chat_list_suggestion_other, null);
                }

                // Locate the TextViews in listview_item.xml

                holder.pictureS = (ImageView) view.findViewById(R.id.picture_text);
                holder.message = (TextView) view.findViewById(R.id.message);


                System.out.println("}}}}}}}}}}}}}}}}}}" + Chats.get(position).getUrl());
                System.out.println("}}}}}}}}}}}}}}}}}}" + Chats.get(position).getSenderEmail());

                Glide.with(mContext).asBitmap()
                        .load(Chats.get(position).getUrl())
                        .into(holder.pictureS);

                view.setTag(holder);


            } else {
                if (Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().toString().trim())) {
                    view = inflater.inflate(R.layout.chat_list_own, null);
                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  chat theme matched" + Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().toString().trim()));
                } else {
                    view = inflater.inflate(R.layout.chat_list_other, null);
                }
                // Locate the TextViews in listview_item.xml
                holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);
                holder.message = (TextView) view.findViewById(R.id.message);
                // holder.date=(TextView)view.findViewById(R.id.date) ;
                holder.time = (TextView) view.findViewById(R.id.time);
                view.setTag(holder);
            }
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        if (Chats != null) {
            try {
                if (Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().trim())) {
                    holder.message.setText(Chats.get(position).getMessage());
                } else {
                    holder.message.setText(Chats.get(position).getMessage());
                    holder.senderEmail.setText(Chats.get(position).getName());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            //holder.date.setText(Chats.get(position).getTime().substring(0,10));
            //  holder.time.setText(Chats.get(position).getTime().substring(10,19));
        }

        return view;
    }

    public class ViewHolder {

        TextView senderEmail;
        TextView message;
        //TextView date ;
        TextView time;
        ImageView pictureS;

    }
}