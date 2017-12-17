package net.netau.vasyoid;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reflector {

    private static void printFields(String indent,
                                    Class<?> someClass,
                                    PrintWriter out) throws IOException {
        for (Field field : someClass.getDeclaredFields()) {
            if (!field.isSynthetic()) {
                out.print(indent);
                String modifiers = Modifier.toString(field.getModifiers());
                if (!modifiers.equals("")) {
                    out.print(modifiers + " ");
                }
                out.print(field.getType().getSimpleName());
                out.println(" " + field.getName() + ";");
                out.println();
            }
        }
    }

    private static void printExceptions(Class<?>[] exceptions,
                                       PrintWriter out) throws IOException {
        if (exceptions.length > 0) {
            out.print(" throws");
            out.print(Arrays.stream(exceptions)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", ", " ", "")));
        }
    }

    private static void printArguments(Class<?>[] arguments,
                                       PrintWriter out) throws IOException {
        out.print(Arrays.stream(arguments)
                .filter(Objects::nonNull)
                .filter(c -> !c.isSynthetic())
                .map(Class::getSimpleName)
                .map(new Function<String, String>() {
                    private int count = 1;
                    @Override
                    public String apply(String s) {
                        return s + " arg" + count++;
                    }
                })
                .collect(Collectors.joining(", ")));
    }

    private static void printMethodHeader(String indent,
                                         Method someMethod,
                                         PrintWriter out) throws IOException {
        out.print(indent);
        String modifiers = Modifier.toString(someMethod.getModifiers());
        if (!modifiers.equals("")) {
            out.print(modifiers + " ");
        }
        out.print(someMethod.getReturnType().getSimpleName());
        out.print(" " + someMethod.getName());
        out.print("(");
        printArguments(someMethod.getParameterTypes(), out);
        out.print(")");
        printExceptions(someMethod.getExceptionTypes(), out);
        out.println(" {");
    }

    private static void printConstructorHeader(String indent,
                                          Constructor someConstructor,
                                          boolean isClassInner,
                                          PrintWriter out) throws IOException {
        out.print(indent);
        String modifiers = Modifier.toString(someConstructor.getModifiers());
        if (!modifiers.equals("")) {
            out.print(modifiers + " ");
        }
        out.print(someConstructor.getDeclaringClass().getSimpleName());
        out.print("(");
        Class<?>[] arguments = someConstructor.getParameterTypes();
        if (isClassInner) {
            arguments[0] = null;
        }
        printArguments(arguments, out);
        out.print(")");
        printExceptions(someConstructor.getExceptionTypes(), out);
        out.println(" {");
    }

    private static void printConstructors(String indent,
                                     Class<?> someClass, boolean isClassInner,
                                     PrintWriter out) throws IOException {
        for (Constructor constructor : someClass.getDeclaredConstructors()) {
            if (!constructor.isSynthetic()) {
                printConstructorHeader(indent, constructor, isClassInner, out);
                out.println(indent + "}");
                out.println();
            }
        }
    }

    private static void printMethods(String indent,
                                    Class<?> someClass,
                                    PrintWriter out) throws IOException {
        for (Method method : someClass.getDeclaredMethods()) {
            if (!method.isSynthetic()) {
                printMethodHeader(indent, method, out);
                Class returnType = method.getReturnType();
                if (returnType == void.class) {
                    out.println();
                } else {
                    out.print(indent + "    " + "return ");
                    if (returnType == boolean.class) {
                        out.println("false;");
                    } else if (returnType.isPrimitive()) {
                        out.println("0;");
                    } else {
                        out.println("null;");
                    }
                }
                out.println(indent + "}");
                out.println();
            }
        }
    }


    private static void printInnerAndNestedClasses(String indent,
                                            Class<?> someClass,
                                            PrintWriter out) throws IOException {
        for (Class<?> clazz : someClass.getDeclaredClasses()) {
            printClass(indent, clazz, !Modifier.isStatic(clazz.getModifiers()), out);
        }
    }

    private static void printClassHeader(String indent,
                                         Class<?> someClass,
                                         PrintWriter out) throws IOException {
        out.print(indent);
        String modifiers = Modifier.toString(someClass.getModifiers());
        if (!modifiers.equals("")) {
            out.print(modifiers + " ");
        }
        out.print(someClass.isInterface() ? "interface" : "class");
        out.print(" " + someClass.getSimpleName());
        Class<?> superclass = someClass.getSuperclass();
        if (superclass != Object.class && superclass != null) {
            out.print(" extends");
            out.print(" " + superclass.getSimpleName());
        }
        Class<?>[] interfaces = someClass.getInterfaces();
        if (interfaces.length > 0) {
            out.print(" implements");
            out.print(Arrays.stream(interfaces)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", ", " ", "")));
        }
        out.println(" {");
        out.println();
    }

    private static void printClass(String indent,
                                   Class<?> someClass,
                                   boolean isInner,
                                   PrintWriter out) throws IOException {
        if (!someClass.isSynthetic()) {
            printClassHeader(indent, someClass, out);
            printFields(indent + "    ", someClass, out);
            printConstructors(indent + "    ", someClass, isInner, out);
            printMethods(indent + "    ", someClass, out);
            printInnerAndNestedClasses(indent + "    ", someClass, out);
            out.println(indent + "}");
            out.println();
        }
    }

    public static void printStructure(Class<?> someClass) {
        try (PrintWriter out = new PrintWriter(someClass.getSimpleName() + ".java")) {
            printClass("", someClass, false, out);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
