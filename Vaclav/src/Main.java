import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author Vaclav Kosak
 * @version 1.0
 */
class Main {

    private static final String URL = "http://www.imdb.com/title/tt2209764/?ref_=nv_sr_3";
    private static final String CODING = "UTF-8";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            URL url = new URL(URL);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), CODING));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Name: \n");
        System.out.printf("Genres: \n");
        System.out.printf("Release Date: \n");
        System.out.printf("Country: \n");
        System.out.printf("Runtime: \n");
        System.out.printf("Storyline: \n");
        System.out.printf("Ratings: \n");
        System.out.printf("Videos: \n");
        System.out.printf("Photos \n");
    }
}
