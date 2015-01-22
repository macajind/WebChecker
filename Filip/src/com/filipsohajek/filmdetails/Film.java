package com.filipsohajek.filmdetails;

import java.time.Year;

public class Film {
    String[] names;

    Year birthyear;

    String genres[];

    int rating;

    String plot;

    int runtime;

    String[][] authors;

    public Film(String[] names, Year birthyear, String[] genres, int rating, String plot, int runtime, String[][] authors) {
        this.names = names;
        this.birthyear = birthyear;
        this.genres = genres;
        this.rating = rating;
        this.plot = plot;
        this.runtime = runtime;
        this.authors = authors;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public Year getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(Year birthyear) {
        this.birthyear = birthyear;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String[][] getAuthors() {
        return authors;
    }

    public void setAuthors(String[][] authors) {
        this.authors = authors;
    }
}
