package cz.sycha.jspider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is used for loading of online files into variables
 *
 * @author Jacob Sycha <jakubsycha@gmail.com>
 * @version 1.0
 */

public class Loader {

    /**
     *
     * Fetches the specified URL as a string, this suitable only for text files like TXT's, XML and JSON, HTML Pages etc.
     *
     * @param URL Url you want to fetch from.
     * @return The fetched content in String format.
     */
    public String loadFromURL (String URL) {

        // Uncomment this while debugging...
        //System.out.println("Requested URL: " + URL);

        StringBuilder sb = new StringBuilder();
        URLConnection urlConn;
        InputStreamReader in = null;
        try {
            URL url = new URL(URL);
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }			}
            in.close();
        }
        catch (Exception e) {
            throw new RuntimeException("Exception while trying to fetch from URL: "+ URL, e);
        }  		return sb.toString();
    }
}
