package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ondřej on 21. 1. 2015.
 */
public class DataDownloader {

    /**
     *
     * Download the file between defined lines in one string
     * @param address Address of text file on web
     * @param newLine Add new line after every download line
     * @param fromLine Download file from line
     * @param toLine Download file to line
     * @return Text file in string array
     */
    public static String downloadTextFile(String address, boolean newLine, int fromLine, int toLine) {
        String result = "";
        String line;
        InputStream inputStream = null;
        URL url;
        BufferedReader bufferedReader;
        try {
            url = new URL(address);
            inputStream = url.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (((fromLine + toLine) == 0) || (i >= fromLine && i <= toLine)) {
                    result += line;
                    if (newLine)
                        result += '\n';
                    i++;
                } else if (i < fromLine)
                    i++;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * Download the whole file in one string
     * @param address Address of file
     * @param newLine Add new line after every download file
     * @return Download text file
     */
    public static String downloadTextFile(String address, boolean newLine){
        return DataDownloader.downloadTextFile(address, newLine, 0, 0);
    }

    /**
     * Download the file between defined lines, where every new line is on her own string
     * @param address Address of file
     * @param fromLine Download file from line
     * @param toLine Download file to line
     * @return Download text file
     */
    public static String[] downloadTextFile(String address, int fromLine, int toLine){
        ArrayList<String> result = new ArrayList<String>();
        String line;
        InputStream inputStream = null;
        URL url;
        BufferedReader bufferedReader;
        try {
            url = new URL(address);
            inputStream = url.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if(((fromLine + toLine) == 0) || (i >= fromLine && i <= toLine)) {
                    result.add(line);
                    i++;
                }else if(i < fromLine)
                    i++;
            }
       } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toArray(new String[]{});
    }
    /**
     * Download the whole file, where every new line is on her own string
     * @param address Address of file
     * @return Download text file
     */
    public static String[] downloadTextFile(String address){
        return DataDownloader.downloadTextFile(address, 0, 0);
    }
}
