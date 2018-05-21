package net.netau.vasyoid;

import net.netau.vasyoid.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static net.netau.vasyoid.TestRunner.TestResult.TestVerdict.*;

public class TestRunner {

    private final Class<?> testClass;
    private Object testInstance = null;
    private final Object[] beforeClassMethods;
    private final Object[] afterClassMethods;
    private final Object[] beforeMethods;
    private final Object[] afterMethods;
    private final Object[] testMethods;

    public TestRunner(Class<?> testClass) {
        this.testClass = testClass;
        beforeClassMethods = getAnnotatedMethods(BeforeClass.class);
        afterClassMethods = getAnnotatedMethods(AfterClass.class);
        beforeMethods = getAnnotatedMethods(Before.class);
        afterMethods = getAnnotatedMethods(After.class);
        testMethods = getAnnotatedMethods(Test.class);
    }

    private Object[] getAnnotatedMethods(Class annotation) {
        // noinspection unchecked
        return Arrays.stream(testClass.getMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .toArray();
    }

    private void runMethods(@NotNull Object[] methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Object method : methods) {
            ((Method) method).invoke(testInstance);
        }
    }

    private List<TestResult> runTests() {
        List<TestResult> results = new LinkedList<>();
        for (Object method : testMethods) {
            Method test = (Method) method;
            TestResult testResult = new TestResult();
            results.add(testResult);
            testResult.name = test.getName();
            Test annotation = test.getAnnotation(Test.class);
            if (!annotation.ignore().isEmpty()) {
                testResult.verdict = IGNORED;
                testResult.message = annotation.ignore();
                continue;
            }
            long startTime = System.currentTimeMillis();
            try {
                runMethods(beforeMethods);
                test.invoke(testInstance);
                runMethods(afterMethods);
                testResult.verdict = OK;
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (annotation.expected().isAssignableFrom(cause.getClass())) {
                    testResult.verdict = OK;
                } else {
                    testResult.verdict = FAILED;
                    testResult.message = cause.getMessage();
                }
            } catch (Exception e) {
                testResult.verdict = FAILED;
                testResult.message = e.getMessage();
            } finally {
                long endTime = System.currentTimeMillis();
                testResult.time = endTime - startTime;
            }
        }
        return results;
    }

    public List<TestResult> runAll()
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        testInstance = testClass.newInstance();
        runMethods(beforeClassMethods);
        List<TestResult> result = runTests();
        runMethods(afterClassMethods);
        return result;
    }

    public static class TestResult {

        private String name;
        private TestVerdict verdict;
        private String message = "";
        private long time = 0;

        public String getName() {
            return name;
        }

        public TestVerdict getVerdict() {
            return verdict;
        }

        public String getMessage() {
            return message;
        }

        public long getTime() {
            return time;
        }

        public enum TestVerdict {
            OK, FAILED, IGNORED
        }

    }

}
