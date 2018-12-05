package ca.jimlong.regextest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.jimlong.regextest.Models.Settings;

public final class App {

    private Settings settings;
    private final static String settingsFile = "settings.json";


    private App() {
        File file = new File(getClass().getResource(settingsFile).getFile());
        this.settings = new Settings(file);
    }

    private ArrayList<String> readTextFile(File file) {

        ArrayList<String> arr = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(file.toPath().toString())))
        {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                arr.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return arr;

    }

    private class TestResult {
        String testName = "";
        Integer testNumber = 0;
        Boolean testPassed = false;

        TestResult(String testName, Integer testNumber, Boolean testPassed) {
            this.testName = testName;
            this.testNumber = testNumber;
            this.testPassed = testPassed;
        }

        public String toString () {
            return "Test:\t" + testNumber.toString() + "\tName:\t" + String.format("%-30s", testName) + "\tResult:\t" + (testPassed ? "Passed" : "Failed");
        }
    }

    private TestResult processLog(String log) {
        Pattern pattern = Pattern.compile(".*?TestExecutor:.*?Test (.*):(.*)(PASSED|FAILED).");
        Matcher matcher = pattern.matcher(log);

        if (!matcher.find()) {
            return null;
        }

        String testNumber = matcher.group(1);
        String testName = matcher.group(2);
        Boolean testPassed = matcher.group(3).equals("PASSED");
        TestResult testResult = new TestResult(testName, Integer.parseInt(testNumber), testPassed);

        return testResult; 

    }

    public static void main(String[] args) {
        App app = new App();

        System.out.println("The log filename is " + app.settings.getLogFilename());

        File file = new File(app.settings.getLogFilename());

        ArrayList<String> logs = app.readTextFile(file);

        ArrayList<TestResult> testResults = new ArrayList<TestResult>();
        for (String log : logs) {
            TestResult t = app.processLog(log);
            if (t != null) {
                testResults.add(t);
            }
        }

        for (TestResult t : testResults) {
            if (!t.testPassed) {
                System.out.println(t.toString());
            }
        }


    }

}
