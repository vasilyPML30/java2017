package net.netau.vasyoid.classes;

public class ClassWithGenericFields<T> {

    java.util.List<? extends T> list;

    MyPair<? super T, ? extends T> point;

    ClassWithGenericFields() {
    }

}


