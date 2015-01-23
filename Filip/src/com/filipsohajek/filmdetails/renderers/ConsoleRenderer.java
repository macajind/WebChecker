package com.filipsohajek.filmdetails.renderers;

import com.filipsohajek.filmdetails.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConsoleRenderer implements IRenderer {

    /**
     * Hashmap containing translations of author array keys
     */
    private HashMap<String, String> authorlex = new HashMap<String, String>();

    /**
     * Variable containing Film object
     */
    private Film film;

    /**
     * Constructor of class
     */
    public ConsoleRenderer() {}

    /**
     * Constructor of class. Accepts film as first parameter
     * @param film Film object to work with
     */
    public ConsoleRenderer(Film film)
    {
        this.film = film;
        authorlex.put("directors", "Režie");
        authorlex.put("original", "Námět");
        authorlex.put("script", "Scénář");
        authorlex.put("camera", "Kamera");
        authorlex.put("soundtrack", "Hudba");
        authorlex.put("actors", "Herci");
    }

    /**
     * Render complete film details
     */
    @Override
    public void renderFilm() {
        renderNames();
        renderBirth();
        renderGenres();
        renderRuntime();
        renderRating();
        renderPlot();
        renderAuthors();
    }

    /**
     * Render film's birth year
     */
    @Override
    public void renderBirth() {
        System.out.println("Rok vydání : " + film.getBirthyear().toString() + "\n");
        System.out.println();
    }

    /**
     * Render film's names
     */
    @Override
    public void renderNames() {
        HashMap<String, String> names = film.getNames();
        Iterator iter = names.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry) iter.next();
            System.out.println("Jméno (" + pairs.getKey() + ") : " + pairs.getValue());
            iter.remove();
        }
        System.out.println();
    }

    /**
     * Render film's plot
     */
    @Override
    public void renderPlot() {
        System.out.println(film.getPlot());
        System.out.println();
    }

    /**
     * Render film's genres
     */
    @Override
    public void renderGenres() {
        System.out.println("Žánry : ");
        ArrayList<String> genres = film.getGenres();
        for (String s : genres)
        {
            System.out.println(s);
        }
        System.out.println();
    }

    /**
     * Render film's rating
     */
    @Override
    public void renderRating() {
        System.out.println("Hodnocení : " + String.valueOf(film.getRating()));
        System.out.println();
    }

    /**
     * Render film's runtime
     */
    @Override
    public void renderRuntime() {
        System.out.println("Délka : " + String.valueOf(film.getRuntime()));
        System.out.println();
    }

    /**
     * Render film's authors
     */
    @Override
    public void renderAuthors() {
        HashMap<String, ArrayList<String>> authors = film.getAuthors();
        Iterator iter = authors.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry) iter.next();
            System.out.println(authorlex.get(pairs.getKey()) + " : ");
            for (String s : (ArrayList<String>) pairs.getValue())
            {
                System.out.println(s);
            }
            iter.remove();
            System.out.println();
        }
    }
}
