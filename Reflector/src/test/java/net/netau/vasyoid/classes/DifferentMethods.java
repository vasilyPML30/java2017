package net.netau.vasyoid.classes;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DifferentMethods {

    public class Class1 {
        public void foo() {}
        public void bar() {}
        void foo2() throws IOException {}
        int foo3() { return 0; }
        <T> void foo4(T x) {}
    }

    public class Class2 {
        private void foo() {}
        void bar() {}
        void foo2() throws FileNotFoundException {}
        Integer foo3() { return 0; }
        <E> void foo4(E x) {}
    }

}
