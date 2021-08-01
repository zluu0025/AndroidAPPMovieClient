package com.example.movieclient.utils;

import android.util.Log;

import com.example.movieclient.bean.Credential;
import com.example.movieclient.bean.Memoir;
import com.example.movieclient.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    public static String Moive_Path ="https://api.themoviedb.org/3/search/movie";
    public static String SERVER_URL ="http://192.168.0.11:8080/MovieMemoir/webresources/";
    public static String KEY ="9d301418d6e3f39e5a053881e7b51f18";
    public static String IMAGE ="https://image.tmdb.org/t/p/w500";
    public static String CREDENTIAL_PATH =SERVER_URL+"memoirclasssource.credential";
    public static String USER_PATH =SERVER_URL+"memoirclasssource.memoiruser";
    public static String CINEMA_PATH =SERVER_URL+"memoirclasssource.cinema";
    public static String MEMOIR_PATH =SERVER_URL+"memoirclasssource.memoir";
    private OkHttpClient client = null;

    MediaType type =  MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    public String getRequest(String url){
        Log.e("url-request", url);
        String result = "";
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Headers.Builder header = new Headers.Builder();
        header.add("Content-Type","application/json");
        header.add("Accept","application/json");
        Request request = builder.headers(header.build()).build();
        try {
            Response response = client.newCall(request).execute();
            result=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("url-response", result);
        return result;
    }


// register  and  check  the username existed in the credential table or not
    public boolean register(User user){
        String url = CREDENTIAL_PATH + "/findByusername/"+user.getCredentialid().getUsername();
        String str = getRequest(url);
        Type gsonType = new TypeToken<List<Credential>>(){}.getType();
        List<Credential> list = new Gson().fromJson(str, gsonType);
        // check username in the servserside table or not
        if (list!=null&&list.size()>0){
            return false;
        }

        // One to One
        int credentialId = getCredentialID()+1;
        user.getCredentialid().setCredentialid(credentialId);
        user.setId(credentialId);


        Request.Builder credentialRequestBuilder = new Request.Builder();
        credentialRequestBuilder.url(CREDENTIAL_PATH);
        // This method serializes the specified object into its equivalent Json representation.
        String cJson = new Gson().toJson(user.getCredentialid());

        //post with run with new string represent in the Jason_format
        Request credential_request = credentialRequestBuilder.post(RequestBody.create( cJson, type)).build();

        try {
            client.newCall(credential_request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String userJson = new Gson().toJson(user);
        Request.Builder userBuilder = new Request.Builder();
        userBuilder.url(USER_PATH);

        // post with run with new string represent in the Jason_format
        Request uRequest = userBuilder.post(RequestBody.create( userJson, type)).build();
        try {
            Response response = client.newCall(uRequest).execute();
            int code = response.code();
            Log.e("Response",response.toString());
            if(code == 204) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }


// use the count method getCredentialID
    private int getCredentialID(){
        int result = 0;
        Request.Builder builder = new Request.Builder();
        builder.url(CREDENTIAL_PATH + "/count");
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            result=Integer.parseInt(response.body().string());
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

//get the user first name for welcome message
    public String firstName(int credentialid){
        String result = "";
        Request.Builder builder = new Request.Builder();
        builder.url(USER_PATH +"/findByCredentialID/"+ credentialid);
        Request request =builder.build();
        try {
            Response response = client.newCall(request).execute();
            result=response.body().string();
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }




// start testing the addMemoir method----
    public String addMemoir(Memoir memoir) {
        String result = "";
        String memoirJson = new Gson().toJson(memoir);
        Request.Builder credentialRequestBuilder = new Request.Builder();
        credentialRequestBuilder.url(MEMOIR_PATH);

        RequestBody body = RequestBody.create( memoirJson,type );
        Request credentialRequest = credentialRequestBuilder.post(body).build();
        try {
            Response response = client.newCall(credentialRequest).execute();
            result=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
