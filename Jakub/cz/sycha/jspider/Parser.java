package cz.sycha.jspider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Parser {

    private Loader loader = new Loader();

    public void getMovie(String movie) {
        String html = this.loader.loadFromURL(movie);
        Document doc = Jsoup.parse(html);

        Element title = doc.select("div.zakladni_info > div.left > h1 > a").first();
        Element origTitle = doc.select("div.text > div.infotext > h2.title_next").first();

        System.out.println("Český název: " + title.ownText());
        System.out.println("Originální název: " + origTitle.ownText());

    }
}
