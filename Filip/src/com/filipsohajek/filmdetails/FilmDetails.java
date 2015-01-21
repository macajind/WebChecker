package com.filipsohajek.filmdetails;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FilmDetails {
    public static void main(String args[]) throws IOException {
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
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        String json = out.toString();
    }
}
