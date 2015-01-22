package com.company;

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

    /**
     * Parse file, to format for needs of this app
     * @param file HTML file
     * @return formatted file
     */
    private String[] parseFile(String[] file){
        ArrayList<String> parsedFile = new ArrayList<String>();
        for(String line : file){
            if(line.trim().length() != 0)
                parsedFile.add(String.format("%d:%s", calculateNesting(line), line.trim()));
        }
        return parsedFile.toArray(new String[]{});
    }

    /**
     * Get content of pair element
     * @param element tag of element (<p>, <a href=''>, etc.) without brackets
     * @param attributes attributes of element
     * @return content of element
     */
    public String[] getContentOfElement(String element, String attributes){
        String searchElement;
        if(!attributes.isEmpty())
            searchElement = String.format("<%s %s>", element, attributes);
        else
            searchElement = String.format("<%s>", element);
        int nestedElements = 0;
        boolean elementFound = false;
        ArrayList<String> contentOfElement = new ArrayList<String>();
        for(String line : file){
            if(!elementFound){
                if(line.contains(searchElement)){
                    String temp;
                    elementFound = true;
                    nestedElements = Integer.parseInt(line.split(":")[0]);
                    temp = line.replace(String.format("%d:%s", nestedElements, searchElement), "");
                    if(!temp.isEmpty()){
                        if(temp.contains(String.format("</%s>", element))){
                            temp = temp.replace(String.format("</%s>", element), "");
                            contentOfElement.add(temp);
                            break;
                        }
                    }
                }
            }else {
                if(line.contains(String.format("%d:</%s>", nestedElements, element))){
                    break;
                }
                contentOfElement.add(line.replace((line.split(":")[0] + ":"), ""));
            }
        }
        return contentOfElement.toArray(new String[]{});
    }

    /**
     * Calculate nesting of element, by counting '\t' on line
     * @param element element
     * @return nesting
     */
    private int calculateNesting(String element){
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