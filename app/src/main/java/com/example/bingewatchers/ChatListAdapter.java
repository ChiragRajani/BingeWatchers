package com.example.bingewatchers;

import android.content.Context;
import android.util.Log;
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
    public static String TAG = "CHAT LIST ADAPTER";
    Context mContext;
    LayoutInflater inflater;
    FirebaseAuth mAuth;
    private List<Message> Chats = null;
    private ArrayList<Message> arraylist;


    public ChatListAdapter(Context context, ArrayList<Message> Chats) {
        mContext = context;
        this.Chats = Chats;
        inflater = LayoutInflater.from(mContext);
        mAuth = FirebaseAuth.getInstance();
        this.arraylist = new ArrayList<Message>();
        this.arraylist = Chats;
//        this.arraylist.addAll(Chats);
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
            if (Chats.get(position).getType().trim().equals("Suggestion")) {

                Log.d(TAG,
                        "        COMPARE                  " + Chats.get(position).getSenderEmail() + "==" + mAuth.getCurrentUser().getEmail().toString());

                if (Chats.get(position).getSenderEmail().trim().equals(mAuth.getCurrentUser().getEmail().toString().trim())) {
                    view = inflater.inflate(R.layout.chat_list_suggestion, null);

                    holder.pictureS = (ImageView) view.findViewById(R.id.picture_text);
                    holder.message = (TextView) view.findViewById(R.id.message);

                    holder.message.setText(Chats.get(position).getMessage());
                    Glide.with(mContext).asBitmap()
                            .load(Chats.get(position).getUrl())
                            .into(holder.pictureS);

//                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  IMAGE" + Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().toString().trim()));
                } else {
                    view = inflater.inflate(R.layout.chat_list_suggestion_other, null);

                    holder.pictureS = (ImageView) view.findViewById(R.id.picture_text);
                    holder.message = (TextView) view.findViewById(R.id.message);
                    holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);

                    Glide.with(mContext).asBitmap()
                            .load(Chats.get(position).getUrl())
                            .into(holder.pictureS);
                    holder.message.setText(Chats.get(position).getMessage());
                    holder.senderEmail.setText(Chats.get(position).getName());
                }
                // Locate the TextViews in listview_item.xml
//
//                holder.pictureS = (ImageView) view.findViewById(R.id.picture_text);
//                holder.message = (TextView) view.findViewById(R.id.message);
                Log.d(TAG, "}}}}}}}}}}}}}}}}}}" + Chats.get(position).getUrl());
                Log.d(TAG, "}}}}}}}}}}}}}}}}}}" + Chats.get(position).getSenderEmail());


            } else {
                if (Chats.get(position).getSenderEmail().equals(mAuth.getCurrentUser().getEmail().toString().trim())) {
                    view = inflater.inflate(R.layout.chat_list_own, null);
                    Log.d(TAG, Chats.get(position).getSenderEmail() + " == " + (mAuth.getCurrentUser().getEmail().toString().trim()));

                    holder.message = (TextView) view.findViewById(R.id.message);

                    holder.message.setText(Chats.get(position).getMessage());


                } else {
                    view = inflater.inflate(R.layout.chat_list_other, null);

                    holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);
                    holder.message = (TextView) view.findViewById(R.id.message);

                    holder.message.setText(Chats.get(position).getMessage());
                    holder.senderEmail.setText(Chats.get(position).getName());
                }
                // Locate the TextViews in listview_item.xml

            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        Chats = new ArrayList<>();
//        notifyDataSetChanged();
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