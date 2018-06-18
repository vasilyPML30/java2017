package net.netau.vasyoid;

import net.netau.vasyoid.classes.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static net.netau.vasyoid.TestRunner.TestResult.TestVerdict.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyJunitTest {

    @Test
    public void afterClassTestTest() throws Exception {
        TestRunner testRunner = new TestRunner(AfterClassTest.class);
        try {
            testRunner.runAll();
        } catch (InvocationTargetException e) {
            assertEquals("ACM", e.getCause().getMessage());
        }
    }

    @Test
    public void beforeAfterTestsTest() throws Exception {
        TestRunner testRunner = new TestRunner(BeforeAfterTests.class);
        List<TestRunner.TestResult> results = testRunner.runAll();
        for (TestRunner.TestResult result : results) {
            assertEquals(FAILED, result.getVerdict());
            switch (result.getName()) {
                case "beforeClassTest":
                    assertEquals("BCM", result.getMessage());
                    break;
                case "beforeTest":
                    assertEquals("BM", result.getMessage());
                    break;
                case "after":
                    assertEquals("AM", result.getMessage());
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public void emptyTestClassTest() throws Exception {
        TestRunner testRunner = new TestRunner(EmptyTestClass.class);
        assertTrue(testRunner.runAll().isEmpty());
    }

    @Test(expected = IllegalAccessException.class)
    public void failingTestClassTest() throws Exception {
        TestRunner testRunner = new TestRunner(FailingTestClass.class);
        testRunner.runAll();
    }

    @Test
    public void FailingTestsTest() throws Exception {
        TestRunner testRunner = new TestRunner(FailingTests.class);
        List<TestRunner.TestResult> results = testRunner.runAll();
        for (TestRunner.TestResult result : results) {
            assertEquals(FAILED, result.getVerdict());
            switch (result.getName()) {
                case "unexpectedExceptionTest":
                    assertEquals("NPE", result.getMessage());
                    break;
                case "expectedWrongExceptionTest":
                    assertEquals("IOE", result.getMessage());
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public void IgnoredTestsTest() throws Exception {
        TestRunner testRunner = new TestRunner(IgnoredTests.class);
        List<TestRunner.TestResult> results = testRunner.runAll();
        for (TestRunner.TestResult result : results) {
            assertEquals(IGNORED, result.getVerdict());
            assertEquals("Test is ignored", result.getMessage());
        }
    }

    @Test
    public void passingTestsTest() throws Exception {
        TestRunner testRunner = new TestRunner(PassingTests.class);
        List<TestRunner.TestResult> results = testRunner.runAll();
        for (TestRunner.TestResult result : results) {
            assertEquals(OK, result.getVerdict());
        }
    }

    @Test
    public void mainTest() throws Exception {
        Main.main(new String[]{".", "net.netau.vasyoid.classes.PassingTests"});
    }

}
