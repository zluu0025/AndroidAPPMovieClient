package com.example.movieclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieclient.utils.MD5Utils;
import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.bean.Credential;
import com.example.movieclient.utils.SP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText etPassword;
    private NetworkConnection networkConnection;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        networkConnection = new NetworkConnection();
        dialog = new ProgressDialog(this);
        et_username = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_login:
                String email = et_username.getText().toString().toLowerCase().trim();
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this,"Email cannot be empty",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,"password cannot be empty",Toast.LENGTH_SHORT).show();
                    return ;
                }
                dialog.show();
                new LoginTask().execute(email,password);
                break;
        }
    }


    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String pwd = params[1];
            Log.e("Email",email);
            Log.e("pwd",pwd);
            Log.e("pwd", MD5Utils.toMD5(pwd));
            String url = NetworkConnection.CREDENTIAL_PATH+"/login"+"/"+email+"/"+MD5Utils.toMD5(pwd);
            return networkConnection.getRequest(url);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Type type = new TypeToken<List<Credential>>(){}.getType();
            List<Credential> list = new Gson().fromJson(result, type);
            if (list!=null&&list.size()>0){
                Credential user = list.get(0);
                SP.getInstance(LoginActivity.this).putInt("ID",user.getCredentialid());
                SP.getInstance(LoginActivity.this).putString("NAME",user.getUsername());
                Toast.makeText(LoginActivity.this,"Welcome "+user.getUsername(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this,"successful",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this,"Incorrect, Check E-mail or Password",Toast.LENGTH_SHORT).show();
            }
        }

    }





}
