package com.example.movieclient.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;
@Entity
public class Movie implements Serializable {
    @PrimaryKey
    @NonNull
    private int id;
    @ColumnInfo(name="vote_average")
    private double vote_average;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="release_date")
    private String release_date;
    @Ignore
    private String backdrop_path;
    @Ignore
    private String overview;
    @Ignore
    private String poster_path;





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }



    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }


    public String getOverview() {
        return overview;
    }


    public String getPoster_path() {
        return poster_path;
    }


}
