package cz.sycha.jspider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class is used for loading of online files into variables
 *
 * @author Jacob Sycha <jakubsycha@gmail.com>
 * @version 2.0
 */
class Loader {

    private static final String CODING = "UTF-8";

    /**
     * Fetches the specified URL as a string, this suitable only for text files like TXT's, XML and JSON, HTML Pages etc.
     *
     * @param URL Url you want to fetch from.
     * @return The fetched content in String format.
     */
    public String loadFromURL(String URL) {
        InputStream input = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(URL);
            input = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, CODING));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
