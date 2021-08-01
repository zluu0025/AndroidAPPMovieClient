package com.example.movieclient.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieclient.bean.Credential;
import com.example.movieclient.bean.User;
import com.example.movieclient.utils.MD5Utils;
import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends Tool_Bar_Activity implements View.OnClickListener {

    private EditText et_Streetnumber;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etSurname;
    private EditText etFirstName;
    private TextView tvDOB;
    private EditText etAddress;
    private EditText etPostcode;
    private Spinner spState;
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    private NetworkConnection networkConnection;
    private ProgressDialog pdialog;


    private  String checkGender = "Male";
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Sign up");
        networkConnection = new NetworkConnection();

        pdialog = new ProgressDialog(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pwd);
        etConfirmPassword = findViewById(R.id.et_pwd_confirm);
        etSurname = findViewById(R.id.et_Surname);
        etFirstName = findViewById(R.id.et_Firstname);
        et_Streetnumber = findViewById(R.id.et_Streetnumber);
        tvDOB = findViewById(R.id.tv_dob);
        etAddress = findViewById(R.id.et_address);
        spState = findViewById(R.id.sp_state);
        etPostcode = findViewById(R.id.et_postcode);

        findViewById(R.id.tv_dob).setOnClickListener(this);
        findViewById(R.id.btn_up).setOnClickListener(this);

        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar ca = Calendar.getInstance();
                int  mYear = ca.get(Calendar.YEAR);
                final int  mMonth = ca.get(Calendar.MONTH);
                int  mDay = ca.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(
                        SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                 String data = year+"";
                                 int monthadd = month + 1;
                                if (month<10){
                                    data=data+"-0"+monthadd;
                                }else{
                                    data=data+"-"+monthadd;
                                }
                                if (dayOfMonth<10){
                                    data=data+"-0"+dayOfMonth;
                                }else{
                                    data=data+"-"+dayOfMonth;
                                }
                                tvDOB.setText(data);
                            }
                        },
                        mYear, mMonth, mDay).show();
            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.stateArray, android.R.layout.simple_spinner_item);
        spState.setAdapter(adapter);
    }


    public void onRadioButton(View v){
        boolean checked = ((RadioButton)v ).isChecked();
        //check Ratio Button which was clicked
        switch(v.getId())
        {
            case R.id.Rb_male:
                if(checked)
                    checkGender = "Male";
                break;
            case R.id.Rb_f_gender:
                if (checked)
                    checkGender = "Female";
                break;
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_up:
                register();
                break;
        }
    }

    private void register() {
        String email = etEmail.getText().toString().toLowerCase().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String address = etAddress.getText().toString();
        String Streetnumber = et_Streetnumber.getText().toString();
        String state = spState.getSelectedItem().toString();
        String name = etFirstName.getText().toString();
        String surname = etSurname.getText().toString();
        String postcode = etPostcode.getText().toString();
        String gender = checkGender;
        String dob = tvDOB.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this,"Enter Full Email Address Ty ",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this,"Missing Enter Password",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(SignUpActivity.this,"Double Check you password",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (!confirmPassword.equals(password)) {
            Toast.makeText(SignUpActivity.this,"Password does not match",Toast.LENGTH_SHORT).show();
            return;
        }
        Credential credential =   new Credential();
        credential.setPasswordhash(MD5Utils.toMD5(password));
        credential.setUsername(email);
        credential.setSigndate(TimeUtils.getTime(new Date()));
        User user=  new User();
        user.setCredentialid(credential);
        user.setName(name);
        user.setSurname(surname);
        user.setStreetname(address);
        user.setStreetnumber(Streetnumber);
        user.setState(state);
        user.setPostcode(postcode);
        user.setGender(gender);
        user.setDob(dob+"T00:00:00+00:00");

        pdialog.show();
        new SignUpTask().execute(user);
    }


    private class SignUpTask extends AsyncTask<User, Void, Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {

            return networkConnection.register(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {

            pdialog.dismiss();
            if (success) {
                Toast.makeText(SignUpActivity.this,"Register  success",Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(SignUpActivity.this,"Failed,The Email user name has been registered ",Toast.LENGTH_SHORT).show();

            }
        }
    }
}




