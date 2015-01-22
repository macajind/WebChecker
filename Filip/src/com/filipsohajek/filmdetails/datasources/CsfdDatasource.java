package com.filipsohajek.filmdetails.datasources;

import com.filipsohajek.filmdetails.Film;
import com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Iterator;

public class CsfdDatasource implements IDatasource {
    @Override
    public Film getFilmByName(String name) throws IOException, FilmNotFoundException, JSONException {
        URL url = new URL("http://csfdapi.cz/movie?search=" + name.replace(" ", "+"));
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        JSONArray obj = null;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            System.err.println("Unknown I/O error while reading response from server! Dying...");
        }
        String response = out.toString();
        if (response == "[]")
        {
            throw new FilmNotFoundException("Cannot find film of this name!");
        }

        obj = new JSONArray(response);
        return getFilmById(String.valueOf(obj.getJSONObject(0).getInt("id")));
    }

    @Override
    public Film getFilmById(String id) throws IOException, JSONException {
        Film film = new Film();
        String furl = "http://csfdapi.cz/movie/" + String.valueOf(id);
        URL url = new URL(furl);
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            System.err.println("Unknown I/O error while reading response from server! Dying...");
        }
        String response = out.toString();
        JSONObject jsonobj = null;
        JSONObject names = null;
        try {
            jsonobj = new JSONObject(response);
            names = jsonobj.getJSONObject("names");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<?> namekeys = names.keys();
        while(namekeys.hasNext()) {
            String key = (String) namekeys.next();
            String value = names.getString(key);
            film.appendName(key, value);
        }

        film.setBirthyear(Year.parse(String.valueOf(jsonobj.getInt("year"))));
        film.setRuntime(Integer.valueOf(jsonobj.getString("runtime").replaceAll("[^\\d.]", "")));

        JSONArray genres = jsonobj.getJSONArray("genres");
        for (int i = 0; i < genres.length(); i++) {
            film.appendGenre(genres.getString(i));
        }
        film.setPlot(jsonobj.getString("plot"));
        film.setRating(jsonobj.getInt("rating"));

        JSONObject authors = jsonobj.getJSONObject("authors");
        Iterator<?> authorkeys = authors.keys();
        while(authorkeys.hasNext()) {
            String key = (String) authorkeys.next();
            JSONArray value = authors.getJSONArray(key);
            ArrayList<String> authorsarray = new ArrayList<String>();
            for (int i = 0; i < value.length(); i++) {
                authorsarray.add(value.getJSONObject(i).getString("name"));
            }
            film.appendAuthor(key, authorsarray.toArray(new String[authorsarray.size()]));
        }
        return film;
    }
}
