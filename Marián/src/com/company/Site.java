package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Site {

    private URL url;

    public Site(String url){

        try{
            this.url = new URL(url);
        }

        catch (MalformedURLException e){
            System.out.println("Url what you call, isn´t good");
        }

    }

    /**
     * Connection to the web
     *
     */
    public void openConnection(){

        try {
            URLConnection myURLConnection = url.openConnection();
            myURLConnection.connect();
        }
        catch (MalformedURLException e) {
            System.out.println("Url what you call, isn´t good");
        }
        catch (IOException e) {
            System.out.println("Url what you call, not exist");
        }

    }

    public String savePage() throws IOException {
        String line = "", all = "";

        BufferedReader in = null;

        // Open page
        in = new BufferedReader(new InputStreamReader(url.openStream()));

        // Add line of text to the variable all
        while ((line = in.readLine()) != null) {
            all += line;
        }
        return all;
    }
}
