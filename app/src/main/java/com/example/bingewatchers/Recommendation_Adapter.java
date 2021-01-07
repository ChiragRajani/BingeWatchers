package com.example.bingewatchers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Recommendation_Adapter extends RecyclerView.Adapter<Recommendation_Adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private Context mContext;
    ArrayList<Movie> he;

    public Recommendation_Adapter(Context mContext, ArrayList<Movie> he) {
        this.mContext = mContext;
        this.he = he;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestions_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).asDrawable()
                .load(he.get(position).getPoster())
                .into(holder.image);

        holder.name.setText(he.get(position).getMovieName());
        holder.genres.setText(he.get(position).getGenres());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: clicked on an image: " + he.get(position).getId());
                Intent i = new Intent(mContext, MovieInfo.class);
                i.putExtra("MovieID", he.get(position).getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
//                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return he.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name,genres;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            genres = itemView.findViewById(R.id.genres);
        }

    }
}
