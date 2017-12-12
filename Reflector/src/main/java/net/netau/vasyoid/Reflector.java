package net.netau.vasyoid;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reflector {

    private static void printFields(String indent,
                                    Class<?> someClass,
                                    PrintWriter out) throws IOException {
        for (Field field : someClass.getDeclaredFields()) {
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
        Class<?>[] arguments = someMethod.getParameterTypes();
        out.print(Arrays.stream(arguments)
                .map(Class::getSimpleName)
                .map(new Function<String, String>() {
                    private int count = 1;
                    @Override
                    public String apply(String s) {
                        return s + " arg" + count++;
                    }
                })
                .collect(Collectors.joining(", ")));
        out.print(")");
        Class<?>[] exceptions = someMethod.getExceptionTypes();
        if (exceptions.length > 0) {
            out.print(" throws");
            out.print(Arrays.stream(exceptions)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", ", " ", "")));
        }
        out.println(" {");
    }

    private static void printMethods(String indent,
                                    Class<?> someClass,
                                    PrintWriter out) throws IOException {
        for (Method method : someClass.getDeclaredMethods()) {
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


    private static void printInnerAndNestedClasses(String indent,
                                            Class<?> someClass,
                                            PrintWriter out) throws IOException {
        for (Class<?> clazz : someClass.getDeclaredClasses()) {
            printClass(indent, clazz, out);
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
        if (superclass != null) {
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
                                   PrintWriter out) throws IOException {
        printClassHeader(indent, someClass, out);
        printFields(indent + "    ", someClass, out);
        printMethods(indent + "    ", someClass, out);
        printInnerAndNestedClasses(indent + "    ", someClass, out);
        out.println(indent + "}");
        out.println();
    }

    public static void printStructure(Class<?> someClass) {
        try (PrintWriter out = new PrintWriter(someClass.getSimpleName() + ".java")) {
            printClass("", someClass, out);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
