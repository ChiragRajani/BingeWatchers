package com.example.bingewatchers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private static final String TAG = "RecyclerViewAdapter";

  //vars
  private ArrayList<String> mNames;
  private ArrayList<String> mImageUrls;
  private Context mContext;
  private String name;

  public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls, String name) {
    mNames = names;
    mImageUrls = imageUrls;
    mContext = context;
    this.name = name;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {


    Log.d(TAG, "onBindViewHolder: called.");
    Log.d(TAG, "-----------" + mNames);

    Glide.with(mContext).asDrawable()
//                .load(("https://ui-avatars.com/api/background=random?name=" + mNames.get(position)))
            .load(mImageUrls.get(position))
            .into(holder.image);

    holder.name.setText(mNames.get(position));

    holder.image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));
        Intent i = new Intent(mContext, ChatWindow.class);
        i.putExtra("Group Name", mNames.get(position));
        i.putExtra("Name", name);
        mContext.startActivity(i);

//                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public int getItemCount() {
    return mImageUrls.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView image;
    TextView name;

    public ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.image_view);
      name = itemView.findViewById(R.id.name);
    }
  }


}