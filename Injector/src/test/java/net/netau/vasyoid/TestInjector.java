package net.netau.vasyoid;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class TestInjector {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("net.netau.vasyoid.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "net.netau.vasyoid.ClassWithOneClassDependency",
                Collections.singletonList(ClassWithoutDependencies.class)
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "net.netau.vasyoid.ClassWithOneInterfaceDependency",
                Collections.singletonList(InterfaceImpl.class)
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
    }
}
