package com.example.movieclient.bean;

import java.util.Date;

public class Memoir {
    private int memoirid;

    private String moviename;

    private String movierealsedate;

    private String watcheddate;
    private String movietime;

    private String comment;
    private double ratingscore;
    private Cinema cinemaid;
    private User approvedid;

    public String getMovietime() {
        return movietime;
    }

    public void setMovierealsedate(String movierealsedate) {
        this.movierealsedate = movierealsedate;
    }

    public void setWatcheddate(String watcheddate) {
        this.watcheddate = watcheddate;
    }

    public void setMovietime(String movietime) {
        this.movietime = movietime;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
    }

    public User getApprovedid() {
        return approvedid;
    }

    public void setApprovedid(User approvedid) {
        this.approvedid = approvedid;
    }

    public int getMemoirId() {
        return memoirid;
    }

    public void setMemoirId(int memoirId) {
        this.memoirid = memoirId;
    }

    public String getMovieName() {
        return moviename;
    }

    public void setMovieName(String movieName) {
        this.moviename = movieName;
    }



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRatingscore() {
        return ratingscore;
    }

    public void setRatingscore(double ratingscore) {
        this.ratingscore = ratingscore;
    }
}
