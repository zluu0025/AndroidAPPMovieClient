package com.example.movieclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.adapter.MovieAdapter;
import com.example.movieclient.bean.Movie;
import com.example.movieclient.bean.MovieList;
import com.example.movieclient.utils.OnItemClickListener;
import com.example.movieclient.utils.SP;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Tool_Bar_Activity {

    private EditText et_Movie_Key;
    private RecyclerView recyclerview;
    private MovieAdapter adapter;
    private List<Movie> list= new ArrayList();
    private ProgressDialog dialog;
    private NetworkConnection networkConnection;
    private int userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        setTitle("Memoir Search");
        networkConnection = new NetworkConnection();
        userID= SP.getInstance(this).getInt("ID");
        dialog = new ProgressDialog(this);
        recyclerview =  findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this,list);
        recyclerview.setAdapter(adapter);
        et_Movie_Key = findViewById(R.id.etKey);
        findViewById(R.id.btGO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = et_Movie_Key.getText().toString();
                dialog.show();
                new getMovie().execute(key);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Movie item =  list.get(position);
                Intent intent = new Intent(SearchActivity.this, MovieInfoActivity.class);
                intent.putExtra(MovieInfoActivity.INTENT_DETAIL_ID,item.getId());
                startActivity(intent);
            }
        });
    }


    private class getMovie extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {


            String url = NetworkConnection.Moive_Path +"?api_key="+ NetworkConnection.KEY + "&query="+params[0];
            String  result = networkConnection.getRequest(url);
            if (TextUtils.isEmpty(result)) {
                return null;
            }
            MovieList response = new Gson().fromJson(result, MovieList.class);
            if (response!=null&&response.getResults()!=null){
                return response.getResults();
            }
            return null;


        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            dialog.dismiss();
            if (movieList==null) {
                Toast.makeText(SearchActivity.this,"No Related Searching",Toast.LENGTH_SHORT).show();
            } else {
                list.clear();
                list.addAll(movieList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
