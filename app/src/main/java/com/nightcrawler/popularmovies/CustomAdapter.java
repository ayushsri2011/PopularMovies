package com.nightcrawler.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<CustomPojo> list_members = new ArrayList<>();
    private final LayoutInflater inflater;
    private Context context;

    public CustomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //resolutions-- "w92", "w154", "w185", "w342", "w500", "w780", or "original"

        String base_image_url = "https://image.tmdb.org/t/p/w500";
        String poster_path = list_members.get(position).getPosterPath();
        String url = base_image_url + poster_path;
        Picasso.get().load(url).placeholder(R.drawable.ph).into(holder.picture);
        holder.movieName.setText(list_members.get(position).getTitle());
    }

    public void setListContent(ArrayList<CustomPojo> list_members) {
        this.list_members = list_members;
        notifyItemRangeChanged(0, list_members.size());

    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView movieName;
        private ImageView picture;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            picture = itemView.findViewById(R.id.picture);
            movieName = itemView.findViewById(R.id.movieName);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            String poster_path = list_members.get(pos).getPosterPath();
            String title = list_members.get(pos).getTitle();
            String rating = list_members.get(pos).getVoteAverage();
            String release_date = list_members.get(pos).getReleaseDate();
            String overview = list_members.get(pos).getOverview();
            String movieID = list_members.get(pos).getMovieID();
            String category = list_members.get(pos).getCategory();

            Intent intent = new Intent(context, stage3.class);
            Bundle args = new Bundle();
            CustomPojo m = new CustomPojo();
            m.setOverview(overview);
            m.setVoteAverage(rating);
            m.setReleaseDate(release_date);
            m.setPosterPath(poster_path);
            m.setTitle(title);
            m.setMovieID(movieID);
            m.setCategory(category);

            ArrayList<CustomPojo> list = new ArrayList<>();
            list.add(m);

            args.putSerializable("ARRAYLIST", list);
            intent.putExtra("BUNDLE", args);
            intent.putExtra("category", category);
            context.startActivity(intent);


        }
    }

    public void removeAt(int position) {
        list_members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, list_members.size());
    }

}