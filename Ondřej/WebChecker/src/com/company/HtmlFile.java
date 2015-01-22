package com.company;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by Ondřej on 21. 1. 2015.
 * @author Ondřej Štorc
 */
public class HtmlFile {
    private String[] file;
    public HtmlFile(String[] file){
        this.file = parseFile(file);
    }

    /**
     * Getter for 'file'
     * @return file
     */
    public String[] getFile() {
        return file;
    }

    private String[] parseFile(String[] file){
        ArrayList<String> parsedFile = new ArrayList<String>();
        for(String line : file){
            if(line.trim().length() != 0)
                parsedFile.add(String.format("%d|%s", calculateNesting(line), line.trim()));
        }
        return parsedFile.toArray(new String[]{});
    }

    /**
     * Get content of pair element
     * @param element signature of element (<p>, <a href=''>, etc.)
     * @param attributes attributess
     * @return
     */
    private String[] getElement(String element, String attributes){
        String file = this.file.toString();
        String searchElement = String.format("<%s %s>", element, attributes);
        ArrayList<String> elementList = new ArrayList<String>();
        if(file.contains(searchElement)) {
            int indexOfSearchingElement = file.indexOf(searchElement);
          //  file = file.startsWith("<footer>");
        }
        return null;
    }
    public int calculateNesting(String element){
        int nesting = 0;
        for(int i = 0; i < element.length(); i++){
            if(element.charAt(i) == '\t')
                nesting++;
            else if(element.charAt(i) == '<')
                break;
        }
        return nesting;
    }
}