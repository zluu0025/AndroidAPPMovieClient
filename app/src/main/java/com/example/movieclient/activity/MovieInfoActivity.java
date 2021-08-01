package com.example.movieclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.movieclient.R;
import com.example.movieclient.bean.Movie;
import com.example.movieclient.bean.MovieDetail;
import com.example.movieclient.roomdata.MovieDB;
import com.example.movieclient.utils.NetworkConnection;
import com.google.gson.Gson;

import java.util.List;

public class MovieInfoActivity extends Tool_Bar_Activity implements View.OnClickListener {
    private ImageView ivImg;
    private TextView  tvCountry;
    private TextView tvGenres ;
    private TextView  tvTitle;
    private TextView  tvCast;
    private TextView  textView;
    private TextView  tvoverView;
    private RatingBar rb_score;
    private ProgressDialog dialog;
    private MovieDetail movieDetail;
    public static final String  INTENT_DETAIL_ID = "detail";
    private int movieID;
    private NetworkConnection mNetworkConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setTitle("Movie Information");
        mNetworkConnection = new NetworkConnection();
        movieID =  getIntent().getIntExtra(INTENT_DETAIL_ID,-1);
        dialog = new ProgressDialog(this);
        initView();

        dialog.show();
        new InfoTask().execute();
    }

    // Get ready for the view
    private void initView() {
         ivImg =  findViewById(R.id.iv_post);
        tvTitle =  findViewById(R.id.tvTitle);
        findViewById(R.id.bt_watch).setOnClickListener(this);
        findViewById(R.id.bt_memoir).setOnClickListener(this);
        textView =  findViewById(R.id.tv_date);
        tvoverView =  findViewById(R.id.overview);
        tvCast =  findViewById(R.id.tv_cast);
        tvGenres =  findViewById(R.id.tv_genres);
         rb_score = findViewById(R.id.rtb_ave);
        tvCountry = findViewById(R.id.tv_country);


    }

    private void updateUI(MovieDetail movieDetail) {

        Glide.with(this).load(NetworkConnection.IMAGE+movieDetail.getPoster_path()).into(ivImg);

        List<MovieDetail.GenresBean> genres = movieDetail.getGenres();
        MovieDetail.CreditsBean cast = movieDetail.getCredits();
        textView.setText(movieDetail.getRelease_date());
        tvoverView.setText(movieDetail.getOverview());
        List<MovieDetail.ProductionCountriesBean> country = movieDetail.getProduction_countries();
        for (int i = 0; i < country.size(); i++) {
            String name =  country.get(i).getName();
            tvCountry.append(name+ " ");
        }
        rb_score.setProgress((int) movieDetail.getVote_average());
        tvTitle.setText(movieDetail.getTitle());
        for (int i = 0; i < genres.size(); i++) {
            String name =  genres.get(i).getName();
            tvGenres.append(name+ " ");
        }
        List<MovieDetail.CreditsBean.CastBean> castList = cast.getCast();
        for (int i = 0; i < castList.size(); i++) {
            String name =  castList.get(i).getName();
            tvCast.append(name+ " ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_memoir:
                addMemoir();
                break;
            case R.id.bt_watch:
                addWatch();
                break;
        }
    }
    private void addMemoir() {
        if (movieDetail==null){
            return;
        }
        Intent intent = new Intent(this,AddMemoirActivity.class);
        intent.putExtra("item",movieDetail);
        startActivity(intent);
    }
    private void addWatch() {
        if (movieDetail==null){
            return;
        }
        new  WatchTask().execute();
    }
    class WatchTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            List<Movie> list = MovieDB.getInstance(MovieInfoActivity.this).dao().getItemById(movieDetail.getId());
            if (list!=null&&list.size()>0){
                return false;
            }
            Movie  saveBen = new Movie();
            saveBen.setId(movieDetail.getId());
            saveBen.setRelease_date(movieDetail.getRelease_date());
            saveBen.setTitle(movieDetail.getTitle());
            saveBen.setVote_average(movieDetail.getVote_average());
            MovieDB.getInstance(getApplicationContext()).dao().addWatch(saveBen);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            if (flag){
                Toast.makeText(getBaseContext()," Add Successful",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(),"Same movie existed in the Watchlist",Toast.LENGTH_LONG).show();
            }


        }
    }
    private class InfoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = "https://api.themoviedb.org/3/movie/" + movieID + "?" + "api_key="+NetworkConnection.KEY+"&append_to_response=credits";;
           return mNetworkConnection.getRequest(url);

        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (!TextUtils.isEmpty(result)){
                 movieDetail =  new Gson().fromJson(result, MovieDetail.class);
                if (movieDetail!=null){
                    updateUI(movieDetail);
                }
            }
        }


    }



}
