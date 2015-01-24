package cz.sycha.jspider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 * This class is used for parsing various requests like displaying a movie or searching
 *
 * @author Jacob Sycha <jakubsycha@gmail.com>
 * @version 1.0
 */
class Parser {

    private final Loader loader = new Loader();
    public final String[] searchResults = new String[64];

    /**
     * Get the URL info from specified URL and print it out in a console
     *
     * @param URL Name of the URL...
     */
    public void getMovie(String URL) {
        String html = this.loader.loadFromURL(URL);
        Document doc = Jsoup.parse(html);

        /*
         * Here, we parse all the elements and get text from them. If the element is missing, we
         * create one and give it some default text. It looks fugly, but who cares right?
         */
        Element title = doc.select("div.zakladni_info > div.left > h1 > a").first();
        if(title == null) title = new Element(Tag.valueOf("p"), "/").text("Neznámý");
        Element origTitle = doc.select("div.text > div.infotext > h2.title_next").first();
        if(origTitle == null) origTitle = new Element(Tag.valueOf("p"), "/").text("Neznámý");
        Elements meta = doc.select("div.text > div.infotext > div.row");
        if(meta == null) meta = new Elements(0);
        Element plot = doc.select("#zbytek").first();
        if(plot == null) plot = new Element(Tag.valueOf("p"), "/").text("Neznámý");

        System.out.println("Český název: " + title.ownText());
        System.out.println("Originální název: " + origTitle.ownText());

        System.out.println(); //Blank line

        //Iterate over all elements with basic URL description and print them out in Key: Value style...
        for(Element elem : meta) {
            System.out.println(elem.getElementsByClass("left_text").text() + " " + elem.getElementsByClass("right_text").text());
        }

        System.out.println(); //Blank line

        System.out.println("Popis filmu:");
        System.out.println(plot.text().replace("< Zobrazit méně", "").replace("oficiální text distributora,", "").replace("zobrazit všechny obsahy", "").replace("(2)", ""));

        System.out.println(); //Blank line
    }

    /**
     * Search for a movie using the specified name/keywords and return a list of found movies with respective ID's
     * Please use with care, may be buggy!
     *
     * @param query what you want to search for
     */
    public void search(String query) {
        query = query.toLowerCase().replace(" ", "+");

        String url = "http://www.fdb.cz/vyhledavani.html?hledat=" + query;

        String html = loader.loadFromURL(url);
        Document doc = Jsoup.parse(html);

        Elements results = doc.select("div#hle_film > div.v_box");

        int i = 0;

        for(Element res : results) {
            Element link = res.select("div.info > a").first();

            System.out.println("[#" + i + "] " + link.text());

            searchResults[i] = link.attr("href");
            i++;
        }
        System.out.println();
    }
}
