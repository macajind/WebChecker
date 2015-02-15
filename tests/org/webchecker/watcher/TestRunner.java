package org.webchecker.watcher;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ListenerConfigTest.class);
        if(result.wasSuccessful()) {
            System.out.println("Success!");
        } else {
            // print count of failures and their percentage in all tests
            System.out.printf("Oh no, some failures (%d - %f%%) appeared:%n",
                    result.getFailureCount(),
                    (float)result.getFailureCount() / result.getRunCount() * 100);
            result.getFailures()
                    .stream()
                    .forEach(System.out::println);
        }
        System.out.println();
        System.out.println("Run time: " + result.getRunTime() + " millis");
    }
}
