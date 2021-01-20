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
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private final ArrayList<Message> arraylist;
    // Declare Variables
    String TAG = "CHAtLISTADAPTER";
    Context mContext;
    LayoutInflater inflater;
    FirebaseAuth mAuth;
    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
    int MAX_LINES = 5;
    Calendar te = Calendar.getInstance(), he = Calendar.getInstance();
    private List<Message> Chats = null;

    public ChatListAdapter(Context context, ArrayList<Message> Chats) {
        mContext = context;
        this.Chats = Chats;
        inflater = LayoutInflater.from(mContext);
        mAuth = FirebaseAuth.getInstance();
        this.arraylist = new ArrayList<Message>();
        this.arraylist.addAll(Chats);
        he.add(Calendar.DATE, -1);

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
                    holder.message2 = view.findViewById(R.id.message);
                    holder.time = (TextView) view.findViewById(R.id.time);
                    holder.message2.setText(Chats.get(position).getMessage()+"\n");

                }
                if (Chats.get(position).getType().equals("Suggestion")) {
                    view = inflater.inflate(R.layout.chat_list_suggestion, null);
                    holder.poster = view.findViewById(R.id.picture_text);
                    holder.message = view.findViewById(R.id.message);
                    holder.time = (TextView) view.findViewById(R.id.time);

                    // holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message.setText(Chats.get(position).getMessage()+"\n");
                    Glide.with(mContext).asBitmap()
                            .load(Chats.get(position).getUrl())
                            .into(holder.poster);
                }
                if (Chats.get(position).getType().equals("activity")) {
                    view = inflater.inflate(R.layout.chat_activity_message, null);
                    holder.message2 = view.findViewById(R.id.message);
                    holder.time = (TextView) view.findViewById(R.id.time);


                    //holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message2.setText(Chats.get(position).getMessage()+"\n");

                }
            } else {
                if (Chats.get(position).getType().equals("message")) {
                    view = inflater.inflate(R.layout.chat_list_other, null);
                    holder.message2 = view.findViewById(R.id.message);
                    holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);
                    holder.time = (TextView) view.findViewById(R.id.time);


                    //  holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message2.setText(Chats.get(position).getMessage()+"\n");
                    holder.senderEmail.setText(Chats.get(position).getName());

                } else if (Chats.get(position).getType().equals("Suggestion")) {
                    view = inflater.inflate(R.layout.chat_list_suggestion_other, null);
                    holder.poster = view.findViewById(R.id.picture_text);
                    holder.message = view.findViewById(R.id.message);
                    holder.senderEmail = (TextView) view.findViewById(R.id.senderEmail);
                    holder.time = (TextView) view.findViewById(R.id.time);


//                    holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message.setText(Chats.get(position).getMessage()+"\n");
                    holder.senderEmail.setText(Chats.get(position).getName());

                    Glide.with(mContext).asBitmap()
                            .load(Chats.get(position).getUrl())
                            .into(holder.poster);
                }
                if (Chats.get(position).getType().equals("activity")) {
                    view = inflater.inflate(R.layout.chat_activity_message, null);
                    holder.message2 = view.findViewById(R.id.message);
                    holder.time = (TextView) view.findViewById(R.id.time);


//                    holder.time.setText(Chats.get(position).getTime().substring(10, 16));
                    holder.message2.setText(Chats.get(position).getMessage()+"\n");

                }
            }
            if (Chats.get(position).getTime().substring(3, 10).equals(he.getTime().toString().substring(3, 10))) {

                holder.time.setText("Yesterday, " + Chats.get(position).getTime().substring(10, 16));
            } else if (Chats.get(position).getTime().substring(3, 10).equals(Calendar.getInstance().getTime().toString().substring(3, 10))) {
                holder.time.setText("Today, " + Chats.get(position).getTime().substring(10, 16));
            } else {
                holder.time.setText(Chats.get(position).getTime().substring(3, 10) +", "+ Chats.get(position).getTime().substring(10, 16));
            }
            if(holder.message!=null)
                holder.message.setShowingLine(6);

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


        TextView senderEmail,message2;
        ShowMoreTextView message;

        ImageView poster;
        TextView time;

    }
}