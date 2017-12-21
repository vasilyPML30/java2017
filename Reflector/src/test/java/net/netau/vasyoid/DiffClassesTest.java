package net.netau.vasyoid;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


public class DiffClassesTest {

    private static final String CLASSES_DIR = "src/test/java/net/netau/vasyoid/classes/";
    private static final String CLASSES_PKG = "net.netau.vasyoid.classes.";


    private void checkEqual(String class1, String class2) throws Exception {
        StringWriter out = new StringWriter();
        assertTrue(Reflector.diffClasses(Class.forName(CLASSES_PKG + class1), Class.forName(CLASSES_PKG + class2), out));
        assertEquals("", out.toString());
    }

    private void checkDifferent(String class1, String class2, String diffFile) throws Exception {
        StringWriter out = new StringWriter();
        assertFalse(Reflector.diffClasses(Class.forName(CLASSES_PKG + class1), Class.forName(CLASSES_PKG + class2), out));
        assertEquals(Files.lines(Paths.get(CLASSES_DIR + diffFile))
                .collect(Collectors.joining("\n")), out.toString());
    }

    @Test
    public void identicalClassesTest() throws Exception {
        checkEqual("SimilarClasses$Class1", "SimilarClasses$Class1");
        Reflector.diffClasses(String.class, String.class);
    }

    @Test
    public void similarClassesTest() throws Exception {
        checkEqual("SimilarClasses$Class1", "SimilarClasses$Class2");
    }

    @Test
    public void similarConstructorsTest() throws Exception {
        checkEqual("SimilarConstructors$Class1", "SimilarConstructors$Class2");
    }

    @Test
    public void differentConstructorsTest() throws Exception {
        checkDifferent("DifferentConstructors$Class1", "DifferentConstructors$Class2", "DifferentConstructors.diff");
    }

    @Test
    public void differentMethodsTest() throws Exception {
        checkDifferent("DifferentMethods$Class1", "DifferentMethods$Class2", "DifferentMethods.diff");
    }

    @Test
    public void differentFieldsTest() throws Exception {
        checkDifferent("DifferentFields$Class1", "DifferentFields$Class2", "DifferentFields.diff");
    }

}
