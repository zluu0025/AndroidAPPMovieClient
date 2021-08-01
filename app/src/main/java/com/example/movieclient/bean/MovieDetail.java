package com.example.movieclient.bean;

import java.io.Serializable;
import java.util.List;

public class MovieDetail implements Serializable {



    private int id;
    private String release_date;
    private String overview;
    private String poster_path;
    private String title;
    private double vote_average;
    private CreditsBean credits;
    private List<GenresBean> genres;
    private List<ProductionCountriesBean> production_countries;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }


    public String getPoster_path() {
        return poster_path;
    }


    public String getRelease_date() {
        return release_date;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }


    public CreditsBean getCredits() {
        return credits;
    }

    public void setCredits(CreditsBean credits) {
        this.credits = credits;
    }

    public List<GenresBean> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresBean> genres) {
        this.genres = genres;
    }



    public List<ProductionCountriesBean> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<ProductionCountriesBean> production_countries) {
        this.production_countries = production_countries;
    }



    public static class CreditsBean implements Serializable{
        private List<CastBean> cast;

        public List<CastBean> getCast() {
            return cast;
        }

        public void setCast(List<CastBean> cast) {
            this.cast = cast;
        }

        public static class CastBean implements Serializable{


            private int cast_id;
            private String character;
            private String credit_id;
            private int gender;
            private int id;
            private String name;
            private int order;
            private String profile_path;

            public int getCast_id() {
                return cast_id;
            }

            public void setCast_id(int cast_id) {
                this.cast_id = cast_id;
            }

            public String getCharacter() {
                return character;
            }

            public void setCharacter(String character) {
                this.character = character;
            }

            public String getCredit_id() {
                return credit_id;
            }

            public void setCredit_id(String credit_id) {
                this.credit_id = credit_id;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getProfile_path() {
                return profile_path;
            }

            public void setProfile_path(String profile_path) {
                this.profile_path = profile_path;
            }
        }
    }

    public static class GenresBean implements Serializable {
        /**
         * id : 18
         * name : Drama
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }



    public static class ProductionCountriesBean implements Serializable{

        private String iso_3166_1;
        private String name;

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
