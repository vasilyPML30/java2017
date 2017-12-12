package net.netau.vasyoid;

import java.io.IOException;
import java.io.Serializable;

public class SomeClass implements Serializable {

    public int asd;
    public Integer bsd;
    private SomeClass dfsdfs;
    protected static Object SDF_SF;

    public static void main(int argc, String[] argv, Object obj) throws IOException {

    }

    private static String foo() {
        return "asd";
    }

    protected static Boolean bar() {
        return true;
    }

    protected static boolean baz() {
        return true;
    }
    protected static double asd() {
        return -42;
    }

    private interface IMyInterface {}
    private static abstract class MyAbstractClass {}
    private static final class MyFinalClass implements IMyInterface {}
    static class MyPackagePrivateClass extends MyAbstractClass {}
    public static class MyPublicClass {}
}
