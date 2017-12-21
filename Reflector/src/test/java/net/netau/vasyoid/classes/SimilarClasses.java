package net.netau.vasyoid.classes;

import java.util.List;

public class SimilarClasses {

    public class Class1<T> {
        int x;
        double y;
        T z;

        Class1() {}
        Class1(T arg) {}

        void foo() {}
        Double bar(List<? extends T> arg) {
            return null;
        }

    }

    public class Class2<T> {
        int x;
        double y;
        T z;

        Class2() {}
        Class2(T arg) {}

        void foo() {}
        Double bar(List<? extends T> arg) {
            return null;
        }

    }


}
