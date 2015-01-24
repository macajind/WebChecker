package cz.sycha.jspider;

import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        // Set the right locale (just to be sure...)
        Locale.setDefault(new Locale("cs_CZ"));

        new Interface();
    }

    public void init() {
        //new Parser().getMovie("http://www.fdb.cz/film/hon-jagten/91831");
        //new Parser().search("hon");
    }
}
