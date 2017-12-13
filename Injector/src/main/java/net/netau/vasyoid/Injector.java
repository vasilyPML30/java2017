package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Injector {

    @NotNull
    private static Object getInstance(@NotNull Class classToInstantiate,
                                      @NotNull HashMap instances)
                                        throws ImplementationNotFoundException {
        if (instances.containsKey(classToInstantiate) &&
                instances.get(classToInstantiate) != null) {
            return instances.get(classToInstantiate);
        }
        Constructor constructor = classToInstantiate.getConstructors()[0];
        Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                .map((Function<Class, Object>) instances::get).toArray();
        try {
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new ImplementationNotFoundException();
        }
    }

    @NotNull
    private static List<Class<?>> getInterfaceImplementations(@NotNull Class<?> interfaceClass,
                                                  @NotNull Iterable<Class<?>> implementations) {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> implementation : implementations) {
            for (Class<?> tInterface : implementation.getInterfaces()) {
                if (tInterface == interfaceClass) {
                    result.add(implementation);
                }
            }
        }
        return result;
    }

    @NotNull
    private static List<Class<?>> getAbstractImplementations(@NotNull Class<?> abstractClass,
                                                 @NotNull Iterable<Class<?>> implementations) {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> implementation : implementations) {
            if (implementation.getSuperclass() == abstractClass) {
                result.add(implementation);
            }
        }
        return result;
    }

    private static void initializeCandidates(@NotNull List<Class<?>> candidates,
                                             @NotNull Iterable<Class<?>> implementations,
                                             @NotNull HashMap<Class, Object> instances)
                                                                        throws Exception {
        if (candidates.isEmpty()) {
            throw new ImplementationNotFoundException();
        } else if (candidates.size() > 1) {
            throw new AmbigiousImplementationException();
        } else {
            initializeRecursively(candidates.get(0), implementations, instances);
        }
    }

    @NotNull
    private static Object initializeRecursively(@NotNull Class classToInitialize,
                                                @NotNull Iterable<Class<?>> implementations,
                                                @NotNull HashMap<Class, Object> instances)
                                                                            throws Exception {
        if (instances.containsKey(classToInitialize)) {
            Object result = instances.get(classToInitialize);
            if (result == null) {
                throw new InjectionCycleException();
            }
            return result;
        }
        instances.put(classToInitialize, null);
        Constructor constructor = classToInitialize.getConstructors()[0];
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (!instances.containsKey(parameterType)) {
                for (Class<?> implementation : implementations) {
                    if (parameterType.isInterface()) {
                        List<Class<?>> candidates = getInterfaceImplementations(parameterType,
                                                                      implementations);
                        initializeCandidates(candidates, implementations, instances);
                    } else if (Modifier.isAbstract(parameterType.getModifiers())) {
                        List<Class<?>> candidates = getAbstractImplementations(parameterType,
                                                                     implementations);
                        initializeCandidates(candidates, implementations, instances);
                    } else {
                        initializeRecursively(implementation, implementations, instances);
                    }
                }
            }
        }
        Object result = getInstance(classToInitialize, instances);
        instances.put(classToInitialize, result);
        return result;
    }

    /**
     * Initialises the class of the given name and returns its instance.
     * @param rootClassName class to instantiate.
     * @param implementations available classes for dependencies resolution.
     * @return instantiated class.
     * @throws Exception when could not resolve dependencies.
     */
    @NotNull
    public static Object initialize(@NotNull String rootClassName,
                                    @NotNull Iterable<Class<?>> implementations)
                                                                throws Exception {
        return initializeRecursively(Class.forName(rootClassName),
                                     implementations, new HashMap<>());
    }

    /**
     * Exception thrown when at least one of dependencies has at least two implementations.
     */
    public static class AmbigiousImplementationException extends Exception {

    }

    /**
     * Exception thrown when at least one of dependencies does not have any implementations.
     */
    public static class ImplementationNotFoundException extends Exception {

    }

    /**
     * Exception thrown when some dependencies are cyclic.
     */
    public static class InjectionCycleException extends Exception {

    }
}
