package cz.sycha.jspider;

public class Main {

    public static void main(String[] args) {
        new Main().init();
    }

    public void init() {
        new Parser().getMovie("http://www.fdb.cz/film/hon-jagten/91831");
    }
}
