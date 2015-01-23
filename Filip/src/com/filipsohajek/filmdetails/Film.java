package com.filipsohajek.filmdetails;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Film {
    /**
     * Hashmap containing film names
     */
    HashMap<String, String> names = new HashMap<String, String>();

    /**
     * Variable containing film's birthyear
     */
    Year birthyear;

    /**
     * ArrayList containing film's genres
     */
    ArrayList<String> genres = new ArrayList<String>();

    /**
     * Variable containing film's rating
     */
    int rating;

    /**
     * Variable containing film's plot
     */
    String plot;

    /**
     * Variable containing film's runtime
     */
    int runtime;

    /**
     * Hashmap conaining film's authors
     */
    HashMap<String, ArrayList<String>> authors = new HashMap<String, ArrayList<String>>();

    public Film()
    {

    }

    public Film(HashMap<String, String> names, Year birthyear, ArrayList<String> genres, int rating, String plot, int runtime, HashMap<String, ArrayList<String>> authors) {
        this.names = names;
        this.birthyear = birthyear;
        this.genres = genres;
        this.rating = rating;
        this.plot = plot;
        this.runtime = runtime;
        this.authors = authors;
    }

    public HashMap<String, String> getNames() {
        return names;
    }

    public void appendName(String name, String lang) {
        this.names.put(lang, name);
    }

    public Year getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(Year birthyear) {
        this.birthyear = birthyear;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = new ArrayList<String>(Arrays.asList(genres));
    }

    public void appendGenre(String genre)
    {
        genres.add(genre);
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

    public HashMap<String, ArrayList<String>> getAuthors() {
        return authors;
    }

    public void appendAuthor(String role, String[] names) {
        this.authors.put(role, new ArrayList<String>(Arrays.asList(names)));
    }
}
