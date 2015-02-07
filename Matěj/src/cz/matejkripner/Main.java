package cz.matejkripner;


import cz.matejkripner.watch.Listener;
import cz.matejkripner.watch.ListenerConfig;
import cz.matejkripner.watch.ListenerGroup;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ListenerGroup g = ListenerGroup.newGroup(() -> {
            try {
                return Jsoup.parse(new File("C:\\zkouska.html"), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, 100);
        Listener
                .listener()
                .element(d -> d.select("p").first())
                .changed((oldElement, newElement) -> !newElement.text().equals(oldElement.text()))
                .action((oldElement, newElement) -> System.out.println("zmena z " + oldElement + " na " + newElement))
                .config(ListenerConfig.defaults().autoCheckingOn(5000))
                .register(g);
        Listener
                .listener()
                .element(d -> d.select("p").get(1))
                .changed((oldElement, newElement) -> !newElement.text().equals(oldElement.text()))
                .action((oldElement, newElement) -> System.out.println("zmena2 z " + oldElement + " na " + newElement))
                .register(g);

        Scanner sc = new Scanner(System.in, "Windows-1250");
        while(true) {
            if(sc.nextLine().equals("check")) {
                g.check();
            }
        }

    }
}
