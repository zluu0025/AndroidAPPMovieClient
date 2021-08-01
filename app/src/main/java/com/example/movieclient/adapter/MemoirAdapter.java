package com.example.movieclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieclient.R;
import com.example.movieclient.bean.Home;

import java.util.List;

public class MemoirAdapter extends RecyclerView.Adapter<MemoirAdapter.ViewHolder> {


    public List<Home> list;
    public Context context;
    public MemoirAdapter(Context context, List<Home> list) {
      this.  list = list;
      this.  context = context;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public MemoirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MemoirAdapter.ViewHolder viewHolder,
                                 final int position) {
        Home home = list.get(position);
        viewHolder.tv_name.setText(home.getMovieName());
        viewHolder.tv_date.setText(home.getReleasedate());
        viewHolder.tv_range.setText(home.getRatingscore());
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

}