package com.filipsohajek.filmdetails.renderers;

import com.filipsohajek.filmdetails.Film;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConsoleRenderer implements IRenderer {

    HashMap<String, String> authorlex = new HashMap<String, String>();

    Film film;

    public ConsoleRenderer() {}
    
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

    @Override
    public void renderBirth() {
        System.out.println("Rok vydání : " + film.getBirthyear().toString() + "\n");
        System.out.println();
    }

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

    @Override
    public void renderPlot() {
        System.out.println(film.getPlot());
        System.out.println();
    }

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

    @Override
    public void renderRating() {
        System.out.println("Hodnocení : " + String.valueOf(film.getRating()));
        System.out.println();
    }

    @Override
    public void renderRuntime() {
        System.out.println("Délka : " + String.valueOf(film.getRuntime()));
        System.out.println();
    }

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
