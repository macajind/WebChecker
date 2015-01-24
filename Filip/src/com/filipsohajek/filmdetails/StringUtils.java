package com.filipsohajek.filmdetails;

public class StringUtils {
    public static String concat(String... pieces)
    {
        StringBuilder result = new StringBuilder();
        for (String piece : pieces) {
            result.append(piece);
        }
        return result.toString();
    }
    public static String capitalizeFirst(String text)
    {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
