package net.netau.vasyoid.classes;

import java.io.IOException;

public class DifferentConstructors {

    public class Class1 {
        Class1() {}
        Class1(Class1 x) {}
        Class1(double x) {}
    }

    public class Class2 {
        Class2(int n) {}
        Class2(Class1 x) {}
        Class2(double x) throws IOException {}
    }

}
