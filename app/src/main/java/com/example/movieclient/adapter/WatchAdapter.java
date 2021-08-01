package com.example.movieclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieclient.R;
import com.example.movieclient.bean.Movie;

import java.util.List;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.ViewHolder> {



    public List<Movie> list;
    public Context context;
    public WatchAdapter(Context context, List<Movie> list) {
        this.  context = context;
      this.  list = list;

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public WatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WatchAdapter.ViewHolder viewHolder,
                                 final int position) {
        Movie movie = list.get(position);
        viewHolder.tv_name.setText(movie.getTitle());
        viewHolder.tv_date.setText(movie.getRelease_date());
        viewHolder.tv_range.setText(movie.getVote_average()+"");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemLongClick(position);
                }
                return true;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_date;
        public TextView tv_range;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date= itemView.findViewById(R.id.tv_date);
            tv_range=itemView.findViewById(R.id.tv_range);
        }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}