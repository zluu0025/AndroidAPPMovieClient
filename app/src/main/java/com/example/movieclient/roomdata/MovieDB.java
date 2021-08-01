package com.example.movieclient.roomdata;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movieclient.bean.Movie;

@Database(entities = {Movie.class}, version = 1,exportSchema=false)
public   abstract class MovieDB extends RoomDatabase {
    public  abstract MovieDao dao();
    public static volatile MovieDB instance;
    public static synchronized MovieDB getInstance(Context context) {
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MovieDB.class,"WatchList")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}