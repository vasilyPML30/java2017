package net.netau.vasyoid;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class PrintStructureTest {
    private static final String CLASSES_DIR = "src/test/java/net/netau/vasyoid/classes/";
    private static final String CLASSES_PKG = "net.netau.vasyoid.classes.";

    private void checkClass(String className) throws Exception {
        StringWriter out = new StringWriter();
        Reflector.printStructure(Class.forName(CLASSES_PKG + className), out);
        assertEquals(Files.lines(Paths.get(CLASSES_DIR + className + ".java"))
                .collect(Collectors.joining("\n")), out.toString());
    }

    private void checkClass(String className, String checkFileName) throws Exception {
        StringWriter out = new StringWriter();
        Reflector.printStructure(Class.forName(CLASSES_PKG + className), out);
        assertEquals(Files.lines(Paths.get(CLASSES_DIR + checkFileName))
                .collect(Collectors.joining("\n")), out.toString());
    }

    @Test
    public void emptyClassTest() throws Exception {
        checkClass("EmptyClass", "EmptyClass.result");
    }

    @Test
    public void classWithConstructorsTest() throws Exception {
        checkClass("ClassWithConstructors");
    }

    @Test
    public void classWithPrivateConstructorsTest() throws Exception {
        checkClass("ClassWithPrivateConstructors");
    }

    @Test
    public void emptyInterfaceTest() throws Exception {
        checkClass("EmptyInterface");
    }

    @Test
    public void classExtendsTest() throws Exception {
        checkClass("ClassExtends");
    }

    @Test
    public void classImplementsTest() throws Exception {
        checkClass("ClassImplements");
    }

    @Test
    public void classExtendsImplementsTest() throws Exception {
        checkClass("ClassExtendsImplements");
    }

    @Test
    public void classWithMethodsTest() throws Exception {
        checkClass("ClassWithMethods", "ClassWithMethods.result");
    }

    @Test
    public void classWithInnerClassesTest() throws Exception {
        checkClass("ClassWithInnerClasses");
    }

    @Test
    public void classWithNestedClassesTest() throws Exception {
        checkClass("ClassWithNestedClasses");
    }

    @Test
    public void abstractClassTest() throws Exception {
        checkClass("AbstractClass");
    }

    @Test
    public void genericClassTest() throws Exception {
        checkClass("GenericClass");
    }

    @Test
    public void classWithGenericConstructorTest() throws Exception {
        checkClass("ClassWithGenericConstructor");
    }

    @Test
    public void classWithMethodThrowsTest() throws Exception {
        checkClass("ClassWithMethodThrows");
    }

    @Test
    public void classWithConstructorThrowsTest() throws Exception {
        checkClass("ClassWithConstructorThrows");
    }

    @Test
    public void classExtendsObjectTest() throws Exception {
        checkClass("ClassExtendsObject", "ClassExtendsObject.result");
    }

    @Test
    public void classExtendsGenericTest() throws Exception {
        checkClass("ClassExtendsGeneric");
    }

}
