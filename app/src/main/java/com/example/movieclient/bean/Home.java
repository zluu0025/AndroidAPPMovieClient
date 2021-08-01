package com.example.movieclient.bean;

public class Home {

    /**
     * MovieName : The Main Event
     * Releasedate : Fri Apr 10 00:00:00 CST 2020
     * Ratingscore : 4.5
     */

    private String MovieName;
    private String Releasedate;
    private String Ratingscore;

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String MovieName) {
        this.MovieName = MovieName;
    }

    public String getReleasedate() {
        return Releasedate;
    }

    public void setReleasedate(String Releasedate) {
        this.Releasedate = Releasedate;
    }

    public String getRatingscore() {
        return Ratingscore;
    }

    public void setRatingscore(String Ratingscore) {
        this.Ratingscore = Ratingscore;
    }
}
