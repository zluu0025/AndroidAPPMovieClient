package com.example.movieclient.bean;

public class Cinema {
    private Integer cinemaid;
    private String cinemaname;
    private String cinemalocation;
    private Short postcode;

    public Integer getCinemaId() {
        return cinemaid;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaid = cinemaId;
    }

    public String getCinemaName() {
        return cinemaname;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaname = cinemaName;
    }

    public String getCinemalocation() {
        return cinemalocation;
    }

    public void setCinemalocation(String cinemalocation) {
        this.cinemalocation = cinemalocation;
    }

    public Short getPostcode() {
        return postcode;
    }

    public void setPostcode(Short postcode) {
        this.postcode = postcode;
    }
}
