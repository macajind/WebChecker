import com.company.Site;

import java.io.IOException;
import java.util.Scanner;

public class WebChecker {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in, "Windows-1250");
        System.out.println("Set link of page:\n");
        String input = sc.nextLine();

        Site site = new Site(input);
        site.openConnection();
        site.savePage();

    }
}

