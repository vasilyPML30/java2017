package net.netau.vasyoid.classes;

public class ClassWithGenericMethods<T> {

    ClassWithGenericMethods() {
    }

    public java.util.List<? super T> bar(java.util.List<? extends T> arg1) {
        return null;
    }

    public final <E> java.util.List<? extends java.util.HashMap<T, E>> baz() {
        return null;
    }

    public static <E> java.util.List<? super E> foo(java.util.List<? extends E> arg1) {
        return null;
    }

}


