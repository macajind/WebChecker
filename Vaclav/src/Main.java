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
        System.out.println("Select Film (imdb.com):");
        String urlString = sc.nextLine();

        Document doc = Jsoup.connect(urlString).get();
        String selectorPagecontent = "div#pagecontent";
        Element contributions = doc.select(selectorPagecontent).first();
        Elements selectedDivs = contributions.select(selectorPagecontent);


        String selectorName = "h1 span";
        String selectorGenres = "div.infobar a:gt(0)";
        String selectorGenres2 = "div.infobar a:gt(2)";
        String selectorDate = "span.nobr a";
        String selectorTime = "div.txt-block time";
        String selectorStory = "div.canwrap p";
        for (Element div : selectedDivs) {
            Element name = div.select(selectorName).first();
            Element genres = div.select(selectorGenres).first();
            Element genres2 = div.select(selectorGenres2).first();
            Element date = div.select(selectorDate).first();
            Element time = div.select(selectorTime).first();
            Element story = div.select(selectorStory).first();
            System.out.println("=====================================");
            System.out.printf("Name: %s \n", name.text());
            System.out.printf("Genres: %s, %s \n", genres.text(), genres2.text());
            System.out.printf("Release date: %s \n", date.text());
            System.out.printf("Runtime: %s\n", time.text());
            System.out.printf("Storyline: %s \n", story.text());
        }
    }
}
