import com.company.Site;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Marián
 * @version 2.0
 */
class WebChecker {

    private static final String CODING = "Windows-1250";

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in, CODING);
        System.out.println("Set link of page:");
        String input = sc.nextLine();
        Site site = new Site(input);
        site.openConnection();
        site.savePage();
    }
}
