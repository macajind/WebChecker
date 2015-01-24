package com.filipsohajek.filmdetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    /**
     * Variable used to hold active HTTP connection
     */
    private HttpURLConnection connection;

    /**
     * A class constructor. Accepts java.net.URL as first parameter. Throws java.io.IOException on error.
     * @param url URL to download
     */
    public HttpClient(URL url) throws IOException {
            connection = (HttpURLConnection) url.openConnection();
    }

    /**
     * Sets whether to follow HTTP redirects
     * @param followRedirects Follow redirects
     */
    public void setFollowRedirects(boolean followRedirects)
    {
        connection.setInstanceFollowRedirects(followRedirects);
    }

    /**
     * Method to get connection input stream. Throws java.io.IOException on error.
     * @return InputStream InputStream of connection
     */
    InputStream getInputStream() throws IOException {
            return connection.getInputStream();
    }

    /**
     * Method to get string representation of response. Throws java.io.IOException on error.
     * @throws java.io.IOException
     * @return String String representation of response
     */
    public String getResponse() throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }
}
