package org.webchecker.tests.forms;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class FormTestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(FormsTest.class, FormTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString() + failure.getTrace());
        }
    }
}