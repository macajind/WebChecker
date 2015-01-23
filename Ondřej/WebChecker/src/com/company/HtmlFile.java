package com.company;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Ondřej Štorc
 * @version 2.0
 */
class HtmlFile {

    private final ArrayList<String> file;

    /**
     *
     * @param file
     */
    public HtmlFile(ArrayList<String> file) {
        this.file = parseFile(file);
    }

    /**
     * Parse file, to format for needs of this app.
     * What happens when file is null?
     *
     * @param file HTML file - very comprehensive
     * @return formatted file - very comprehensive
     */
    private ArrayList<String> parseFile(ArrayList<String> file) {
        return (ArrayList<String>) file.stream().filter(line -> line.trim().length() != 0).map(line -> String.format("%d:%s", calculateNesting(line), line.trim())).collect(Collectors.toList());
    }

    /**
     * Get content of pair element.
     * What happens when any of parameters is null or empty?
     *
     * @param element    tag of element (<p>, <a href=''>, etc.) without brackets
     * @param attributes attributes of element
     * @return content of element
     */
    public ArrayList<String> getContentOfElement(String element, String attributes) {
        String searchElement;
        if (!attributes.isEmpty()) searchElement = String.format("<%s %s>", element, attributes);
        else searchElement = String.format("<%s>", element);
        int nestedElements = 0;
        boolean elementFound = false;
        ArrayList<String> contentOfElement = new ArrayList<>();
        for (String line : file) {
            if (!elementFound) {
                if (line.contains(searchElement)) {
                    String temp;
                    elementFound = true;
                    nestedElements = Integer.parseInt(line.split(":")[0]);
                    temp = line.replace(String.format("%d:%s", nestedElements, searchElement), "");
                    if (!temp.isEmpty()) {
                        if (temp.contains(String.format("</%s>", element))) {
                            temp = temp.replace(String.format("</%s>", element), "");
                            contentOfElement.add(temp);
                            break;
                        }
                    }
                }
            } else {
                if (line.contains(String.format("%d:</%s>", nestedElements, element))) {
                    break;
                }
                contentOfElement.add(line.replace((line.split(":")[0] + ":"), ""));
            }
        }
        return contentOfElement;
    }

    /**
     * Get content of element's attribute.
     * What happens when any of parameters is null or empty?
     *
     * @param element tag of element (<p>, <a href=''>, etc.) without brackets
     * @param uniqueAttribute unique attribute, used to identify the element
     * @param dataAttribute from which attribute you want to get data
     * @return data of an attribute - very comprehensive
     * @trouble Data must be at end of the element.. :( - notation @trouble is not supported!
     */
    public String getContentOfAttribute(String element, String uniqueAttribute, String dataAttribute) {
        String searchElement = String.format("<%s %s %s=", element, uniqueAttribute, dataAttribute);
        for (String line : file) {
            if (line.contains(searchElement)) {
                String temp = line.replace(line.substring(0, line.indexOf(dataAttribute)) + dataAttribute + "=\"", "");
                temp = temp.substring(0, temp.indexOf('"'));
                return temp;
            }
        }
        return null;
    }

    /**
     * Calculate nesting of element, by counting '\t' on line.
     * What happens when element is null?
     *
     * @param element element - very comprehensive
     * @return nesting - missing comment
     */
    private int calculateNesting(String element) {
        int nesting = 0;
        for (int i = 0; i < element.length(); i++) {
            if (element.charAt(i) == '\t') nesting++;
            else if (element.charAt(i) == '<') break;
        }
        return nesting;
    }
}
