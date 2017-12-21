package net.netau.vasyoid.classes;

public class ClassWithNestedClasses {

    public ClassWithNestedClasses() {
    }

    public static final class NestedClass {

        public NestedClass() {
        }

        public class NestedNestedClass {

            public NestedNestedClass() {
            }

        }

    }

    private abstract static interface NestedInterface {

        public abstract static interface NestedNestedInterface {

        }

    }

}


