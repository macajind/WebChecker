import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Vaclav Kosak
 * @version 2.0
 */
class Main {

    private static final String CODING = "UTF-8";

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in, CODING);

        //select of movie
        System.out.println("Select Film (imdb.com):");
        String urlString = sc.nextLine();

        Document doc = Jsoup.connect(urlString).get();

        //Element in html, which will be searched
        String selectorPagecontent = "div#pagecontent";
        Element contributions = doc.select(selectorPagecontent).first();
        Elements selectedDivs = contributions.select(selectorPagecontent);

        //selected html code locates a sequential listing
        for (Element div : selectedDivs) {
            Element name = div.select("h1 span").first();
            Element genres = div.select("div.infobar a:gt(0)").first();
            Element genres2 = div.select("div.infobar a:gt(2)").first();
            Element date = div.select("span.nobr a").first();
            Element time = div.select("div.txt-block time").first();
            Element story = div.select("div.canwrap p").first();
            Elements photos = doc.select("div.combined-see-more a[href]");
            Elements videos = doc.select("div.combined-see-more a:gt(2)[href]");
            System.out.println("=====================================");
            System.out.printf("Name: %s \n", name.text());
            System.out.printf("Genres: %s, %s \n", genres.text(), genres2.text());
            System.out.printf("Release date: %s \n", date.text());
            System.out.printf("Runtime: %s\n", time.text());
            System.out.printf("Storyline: %s \n", story.text());
            System.out.printf("Photos: ");
            System.out.println(photos.attr("abs:href"));
            System.out.printf("Videos: ");
            System.out.println(videos.attr("abs:href"));

        }
    }
}
