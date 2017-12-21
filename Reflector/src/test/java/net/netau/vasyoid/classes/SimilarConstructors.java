package net.netau.vasyoid.classes;

public class SimilarConstructors {

    public class Class1 {
        Class1() {}
        Class1(Class1 x, Class1 y) {}
        Class1(Class2 x) {}
        Class1(Class2 x, int n) {}
    }

    public class Class2 {
        Class2() {}
        Class2(Class2 a, Class2 b) {}
        Class2(Class1 a) {}
        Class2(Class1 a, int m) {}
    }
}
