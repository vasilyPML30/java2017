package net.netau.vasyoid;

public class ClassWithOneInterfaceDependency {

    public final Interface dependency;

    public ClassWithOneInterfaceDependency(Interface dependency) {
        this.dependency = dependency;
    }
}