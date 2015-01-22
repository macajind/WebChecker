package com.filipsohajek.filmdetails;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FilmDetails {

    public static void main(String args[]) throws JSONException, MalformedURLException {
        Map<String, String> authorslex = new HashMap<String, String>();
        authorslex.put("directors", "Režiséři");
        authorslex.put("original", "Námět");
        authorslex.put("script", "Scénář");
        authorslex.put("camera", "Kamera");
        authorslex.put("soundtrack", "Hudba");
        authorslex.put("actors", "Herci");

        int fid = 227786;
        String surl = "http://csfdapi.cz/movie/" + String.valueOf(fid);
        URL url = new URL(surl);
        HttpURLConnection con = null;
        InputStream is;
        try {
            con = (HttpURLConnection) url.openConnection();
            is = con.getInputStream();
        } catch (IOException e) {
            System.err.println("Unknown I/O error! Dying...");
            con.disconnect();
            return;
        }
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
        String json = out.toString();
        JSONObject jsonobj = new JSONObject(json);
        JSONObject names = jsonobj.getJSONObject("names");
        Iterator<?> namekeys = names.keys();
        while(namekeys.hasNext()) {
            String key = (String) namekeys.next();
            String value = names.getString(key);
            System.out.println("Název (" + key + ") : " + value);
        }

        System.out.println("\nRok vydání : " + String.valueOf(jsonobj.getInt("year")) + "\n");
        System.out.println("Délka : " + jsonobj.getString("runtime") + "\n");
        System.out.println("Žánry : ");

        JSONArray genres = jsonobj.getJSONArray("genres");
        for (int i = 0; i < genres.length(); i++) {
            System.out.println(genres.getString(i));
        }
        System.out.println("\nPopis : " + jsonobj.getString("plot") + "\n");
        System.out.println("\nHodnocení uživatelů : " + jsonobj.getInt("rating") + " %\n");

        JSONObject authors = jsonobj.getJSONObject("authors");
        Iterator<?> authorkeys = authors.keys();
        while(authorkeys.hasNext()) {
            String key = (String) authorkeys.next();
            JSONArray value = authors.getJSONArray(key);
            System.out.println(authorslex.get(key) + " : ");
            for (int i = 0; i < value.length(); i++) {
                System.out.println(value.getJSONObject(i).getString("name"));
            }
            System.out.print("\n");
        }

    }

}
