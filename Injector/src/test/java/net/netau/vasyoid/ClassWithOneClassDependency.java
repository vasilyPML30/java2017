package net.netau.vasyoid;

public class ClassWithOneClassDependency {

    public final ClassWithoutDependencies dependency;

    public ClassWithOneClassDependency(ClassWithoutDependencies dependency) {
        this.dependency = dependency;
    }
}