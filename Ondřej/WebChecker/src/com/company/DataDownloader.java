package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Ondřej Štorc
 * @version 2.0
 */
class DataDownloader {

    private static final String CODING = "UTF-8";

    /**
     * Download the file between defined lines, where every new line is on her own {@link String}.
     * What happens when any of the parameters is empty or null?
     *
     * @param address Address of file
     * @return Downloaded text file - very comprehensive
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
