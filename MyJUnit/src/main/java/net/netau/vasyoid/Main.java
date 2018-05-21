package net.netau.vasyoid;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please enter a path to a test class.");
            System.exit(1);
        } else {
            File testFile = new File(args[0]);
            try {
                ClassLoader classLoader = new URLClassLoader(new URL[]{testFile.toURI().toURL()});
                Class testClass = classLoader.loadClass(testFile.getName().split("\\.")[0]);
                //noinspection unchecked
                TestRunner testRunner = new TestRunner(testClass);
                long startTime = System.currentTimeMillis();
                List<TestRunner.TestResult> results = testRunner.runAll();
                long execTime = System.currentTimeMillis() - startTime;
                int ok = 0, failed = 0, ignored = 0, total = results.size();
                for (TestRunner.TestResult result : results) {
                    System.out.println("\n===============\n");
                    System.out.println("Test: " + result.getName());
                    System.out.println("Verdict: " + result.getVerdict());
                    System.out.println("Message: " + result.getMessage());
                    System.out.println("Execution time: " + result.getTime() + "ms");
                    switch (result.getVerdict()) {
                        case OK:
                            ok++;
                            break;
                        case FAILED:
                            failed++;
                            break;
                        case IGNORED:
                            ignored++;
                        default:
                            break;
                    }
                }
                System.out.println("\n===============\n");
                System.out.println(ok + "/" + total + " tests passed");
                System.out.println(failed + "/" + total + " tests failed");
                System.out.println(ignored + "/" + total + " tests ignored");
                System.out.println("Total execution time: " + execTime);
            } catch (MalformedURLException | ClassNotFoundException e) {
                System.out.println("Could not load test class: " + e.getMessage());
                System.exit(1);
            } catch (Exception e) {
                System.out.print("could not execute tests: " + e.getMessage());
            }
        }
    }

}
