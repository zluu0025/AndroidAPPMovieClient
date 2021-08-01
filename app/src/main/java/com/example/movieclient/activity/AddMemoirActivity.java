package com.example.movieclient.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieclient.bean.User;
import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.bean.Cinema;
import com.example.movieclient.bean.Memoir;
import com.example.movieclient.bean.MovieDetail;
import com.example.movieclient.utils.SP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMemoirActivity extends Tool_Bar_Activity implements  AdapterView.OnItemSelectedListener {

    MovieDetail movieDetail;
    private NetworkConnection networkConnection;
    private ProgressDialog dialog;
    private Spinner spCinema;
    private SpinnerCinemaAdapter arrayAdapter;
    private TextView tv_time;
    private RatingBar ratingBar;
    private List<Cinema> list;
    Cinema cinemaid;
    private Integer personID;
    private User user;
    private EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemoir);
        setTitle("Adding Memoir");
        comment = findViewById(R.id.ed_comment);
        personID= SP.getInstance(this).getInt("ID");
        movieDetail = (MovieDetail) getIntent().getSerializableExtra("item");
        networkConnection = new NetworkConnection();
        dialog = new ProgressDialog(this);
        dialog.show();
        new CinemaListTask().execute();
        ImageView iv_post = findViewById(R.id.iv_post);
        spCinema = findViewById(R.id.sp_cinema);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tv_time = findViewById(R.id.watch);
        TextView release_date = findViewById(R.id.tv_date);

        Glide.with(this).load(NetworkConnection.IMAGE+movieDetail.getPoster_path()).into(iv_post);
        tvTitle.setText(movieDetail.getTitle());
        release_date.setText(movieDetail.getRelease_date());
        final TextView tv_time = findViewById(R.id.watch);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });
        ratingBar = findViewById(R.id.rating_bar);



        Button submitBt = findViewById(R.id.btn_submit);
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cinemaid==null){
                    return;
                }
                String tvTime = tv_time.getText().toString();
                if (TextUtils.isEmpty(tvTime)){
                    return;
                }
                Memoir memoir = new Memoir();
                memoir.setMemoirId(movieDetail.getId());
                memoir.setMovieName(movieDetail.getTitle());
                memoir.setMovierealsedate(movieDetail.getRelease_date()+"T00:00:00+00:00");
                memoir.setComment(comment.getText().toString());
                memoir.setCinemaid(cinemaid);
                memoir.setWatcheddate(tvTime+"T00:00:00+00:00");
                memoir.setRatingscore(ratingBar.getRating());
                memoir.setApprovedid(user);
                memoir.setMovietime(tvTime+"T00:00:00+00:00");
                new AddMemoir().execute(memoir);

            }
        });
        spCinema.setOnItemSelectedListener(this);
        new CinemaListTask().execute();
        new UserInfo().execute();
    }


    private class UserInfo extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url =    NetworkConnection. USER_PATH + "/"+personID;
            return networkConnection.getRequest(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (!TextUtils.isEmpty(result)){

                user = new Gson().fromJson(result, User.class);

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cinemaid =   list.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class CinemaListTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url =    NetworkConnection. SERVER_URL + "memoirclasssource.cinema/";
           return networkConnection.getRequest(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (!TextUtils.isEmpty(result)){
                Type type = new TypeToken<List<Cinema>>(){}.getType();
               list = new Gson().fromJson(result, type);
                if (list!=null&&list.size()>0){
                    arrayAdapter =
                            new SpinnerCinemaAdapter(AddMemoirActivity.this,list);
                    spCinema.setAdapter(arrayAdapter);
                }

            }
        }
    }

    public void selectTime() {

        Calendar ca = Calendar.getInstance();
        int  mYear = ca.get(Calendar.YEAR);
        final int  mMonth = ca.get(Calendar.MONTH);
        int  mDay = ca.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(
                AddMemoirActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        String data = year+"";
                        if (month+1<10){
                            data=data+"-0"+month;
                        }else{
                            data=data+"-"+month;
                        }
                        if (dayOfMonth<10){
                            data=data+"-0"+dayOfMonth;
                        }else{
                            data=data+"-"+dayOfMonth;
                        }
                        tv_time.setText(data);
                    }
                },
                mYear, mMonth, mDay).show();



    }


 class AddMemoir extends AsyncTask<Memoir, Void, Boolean>
 {
     @Override
     protected Boolean doInBackground(Memoir... memoirs) {
         try {
             networkConnection.addMemoir(memoirs[0]);
             return true;
         }catch (Exception e){e.printStackTrace();}

         return false;
     }

     @Override
     protected void onPostExecute(Boolean a) {
         super.onPostExecute(a);
         if (a) {
             Toast.makeText(getApplicationContext(),"successfully", Toast.LENGTH_LONG).show();
             finish();
         }
     }
 }
    class SpinnerCinemaAdapter  extends BaseAdapter implements SpinnerAdapter {
        private Activity activity;
        private List<Cinema> list;

        public SpinnerCinemaAdapter(Activity activity, List<Cinema> list) {
            this.activity = activity;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Cinema getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return list.get(position).getCinemaId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            if (convertView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                view = inflater.inflate(R.layout.layout_cinema, parent, false);
            } else {
                view = convertView;
            }
            TextView tv_name =  view.findViewById(R.id.tv_name);
            tv_name.setText(String.valueOf(list.get(position).getCinemaName()));
            return view;
        }
    }


}
