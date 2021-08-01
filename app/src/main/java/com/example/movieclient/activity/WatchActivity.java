package com.example.movieclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieclient.R;
import com.example.movieclient.adapter.WatchAdapter;
import com.example.movieclient.bean.Movie;
import com.example.movieclient.roomdata.MovieDB;

import java.util.ArrayList;
import java.util.List;

public class WatchActivity extends Tool_Bar_Activity {

    private RecyclerView recyclerview;
    private WatchAdapter adapter;
    private List<Movie> list= new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        setTitle("Watch");

        recyclerview =  findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WatchAdapter(this,list);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new WatchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Movie item =  list.get(position);
                Intent intent = new Intent(WatchActivity.this, MovieInfoActivity.class);
                intent.putExtra(MovieInfoActivity.INTENT_DETAIL_ID,item.getId());
                startActivity(intent);
            }


            // AlerDialog delete notes information
            @Override
            public void onItemLongClick(final int position) {
                new AlertDialog.Builder(WatchActivity.this)
                        .setTitle("Alert")
                        .setMessage("delete or not")
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new CancelTask().execute(list.get(position));
                                dialog.dismiss();
                            }
                        })

                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
        new WatchListTask().execute();
    }


    class CancelTask extends AsyncTask<Movie,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Movie... movies) {
            try {
                MovieDB.getInstance(WatchActivity.this).dao().delete(movies[0]);
                return true;
            }
            catch (Exception e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            new WatchListTask().execute();
        }
    }

    class WatchListTask extends AsyncTask<Void,Void, List<Movie>>{
        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return  MovieDB.getInstance(WatchActivity.this).dao().getWatchList();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            list.clear();
            list.addAll(movies);
            adapter.notifyDataSetChanged();
        }
    }
}
