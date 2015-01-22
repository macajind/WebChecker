import com.company.Site;

import java.io.IOException;

public class WebChecker {

    public static void main(String[] args) throws IOException {
        Site site = new Site("http://marianligocky.eu");
        site.openConnection();
        String html = site.savePage();
        System.out.println(html);
    }
}

