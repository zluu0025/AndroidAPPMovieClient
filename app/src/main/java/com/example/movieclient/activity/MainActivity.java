package com.example.movieclient.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.adapter.MemoirAdapter;
import com.example.movieclient.bean.Home;
import com.example.movieclient.fragment.HomeFragment;
import com.example.movieclient.utils.SP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView welcomeMessage;
    private TextView cur_date;
    private DrawerLayout mDrawerLayout;
    private ImageView ivNavLeft;
    NetworkConnection networkConnection;
    private ProgressDialog dialog;
    private RecyclerView recyclerview;
    private MemoirAdapter adapter;
    private List<Home> list = new ArrayList<>();
    private Integer personID;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        personID= SP.getInstance(this).getInt("ID");
        networkConnection = new NetworkConnection();
        dialog = new ProgressDialog(this);
        toolbar = findViewById(R.id.toolbar);
        // welcome and date:
        welcomeMessage= findViewById(R.id.wel_Message);
        cur_date = findViewById(R.id.wel_date);

        mDrawerLayout = findViewById(R.id.drawerlayout);
        recyclerview = findViewById(R.id.recyclerView);
        ivNavLeft = toolbar.findViewById(R.id.iv_nav_left);
        // recyclerview.
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemoirAdapter(this,list);
        recyclerview.setAdapter(adapter);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ivNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, new HomeFragment());
        fragmentTransaction.commit();
        new WelcomeTask().execute(personID);
        displayCurretDate();
        new TopTask().execute();
    }


    private void toggle() {
        int drawerLockMode = mDrawerLayout.getDrawerLockMode(GravityCompat.START);
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mDrawerLayout.openDrawer(GravityCompat.START);

        }
    }

    private class WelcomeTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int i = params[0].intValue();
            return networkConnection.firstName(i);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result.length() > 0) {
                try {
                    JSONObject jasonObject = new JSONObject(result);
                    name = jasonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            String extract_name;
            extract_name = name.replace("[", "");
            name = extract_name.replace("]", " ");
            name = "Hi, " + name + "nice to see you again";
            welcomeMessage.setText(name);
        }

    }

    public void displayCurretDate(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        cur_date.setText(formattedDate);
    }

    private class TopTask extends AsyncTask<Integer, Void, List<Home>> {

        @Override
        protected List<Home> doInBackground(Integer... params) {
            String url =   NetworkConnection.MEMOIR_PATH+"/Task04f/" + personID;
            String result = networkConnection.getRequest(url);
            Type type = new TypeToken<List<Home>>(){}.getType();
            List<Home> list = new Gson().fromJson(result, type);
            return list;


        }

        @Override
        protected void onPostExecute(List<Home> homeList) {
            if (homeList !=null) {
                list.clear();
                list.addAll(homeList);
                adapter.notifyDataSetChanged();
            }

        }
    }
}
