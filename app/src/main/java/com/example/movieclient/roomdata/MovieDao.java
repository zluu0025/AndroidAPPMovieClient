package com.example.movieclient.roomdata;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieclient.bean.Movie;

import java.util.List;

@androidx.room.Dao
public interface MovieDao {
    @Insert
    void addWatch(Movie... movies);
    @Delete
    void delete(Movie... movies);
    @Query("select * from movie")
    List<Movie> getWatchList();

    @Query("select * from movie WHERE id = :id")
    List<Movie> getItemById(int id);
}
