package com.example.movieclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieclient.R;
import com.example.movieclient.bean.Movie;
import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.utils.OnItemClickListener;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public List<Movie> list;
    Context context;
    public MovieAdapter(Context context, List<Movie> list) {
      this.  list = list;
      this.  context = context;
    }
    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder,
                                 final int position) {
        Movie movie = list.get(position);
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvDate.setText(movie.getVote_average()+"");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        Glide.with(context).load(NetworkConnection.IMAGE+movie.getPoster_path()).into(viewHolder.iv_img);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView iv_img;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name);
            tvDate= itemView.findViewById(R.id.tv_data);
            iv_img=itemView.findViewById(R.id.iv_post);
        }

    }

}