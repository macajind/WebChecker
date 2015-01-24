package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class used to download files from the Web. The class uses HTTP to retrieve data
 *
 * @author Ondřej Štorc
 * @version 2.0
 */
class DataDownloader {

    private static final String CODING = "UTF-8";

    /**
     * Downloads text file from the Internet and stores it in the {@link ArrayList}, where each new line will be stored in the new part of the {@link ArrayList}.
     * When any of the parameters is empty or null, application will print {@link java.net.MalformedURLException} stack trace
     *
     * @param address Address of file with http://
     * @return text file at specific address
     */
    public static ArrayList<String> downloadTextFile(String address) {
        ArrayList<String> result = new ArrayList<>();
        InputStream inputStream = null;
        try {
            URL url = new URL(address);
            inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CODING));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
