package net.netau.vasyoid;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reflector {

    private static String getShortTypeName(Package currentPackage, Type type) {
        String result = type.getTypeName();
        if (!result.startsWith(currentPackage.getName())) {
            return result;
        }
        return result.replaceAll(".*([$.])", "");
    }

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
                out.print(getShortTypeName(someClass.getPackage(), field.getGenericType()));
                out.println(" " + field.getName() + ";");
                out.println();
            }
        }
    }

    private static void printExceptions(Package currentPackage,
                                        Type[] exceptions,
                                        PrintWriter out) throws IOException {
        if (exceptions.length > 0) {
            out.print(" throws");
            out.print(Arrays.stream(exceptions)
                    .map(t -> getShortTypeName(currentPackage, t))
                    .collect(Collectors.joining(", ", " ", "")));
        }
    }

    private static void printArguments(Package currentPackage,
                                       Type[] arguments,
                                       PrintWriter out) throws IOException {

        out.print(Arrays.stream(arguments)
                .filter(Objects::nonNull)
                .map(t -> getShortTypeName(currentPackage, t))
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
        printTypeParameters(someMethod.getDeclaringClass().getPackage(), someMethod.getTypeParameters(), out);
        if (someMethod.getTypeParameters().length > 0) {
            out.print(" ");
        }
        out.print(getShortTypeName(someMethod.getDeclaringClass().getPackage(), someMethod.getGenericReturnType()) + " ");
        out.print(someMethod.getName());
        out.print("(");


        printArguments(someMethod.getDeclaringClass().getPackage(), someMethod.getGenericParameterTypes(), out);
        out.print(")");
        printExceptions(someMethod.getDeclaringClass().getPackage(), someMethod.getGenericExceptionTypes(), out);
        out.println(" {");
    }

    private static void printConstructorHeader(String indent,
                                          Constructor someConstructor,
                                          Class<?> outerClass,
                                          PrintWriter out) throws IOException {
        out.print(indent);
        String modifiers = Modifier.toString(someConstructor.getModifiers());
        if (!modifiers.equals("")) {
            out.print(modifiers + " ");
        }
        printTypeParameters(someConstructor.getDeclaringClass().getPackage(), someConstructor.getTypeParameters(), out);
        if (someConstructor.getTypeParameters().length > 0) {
            out.print(" ");
        }
        out.print(someConstructor.getDeclaringClass().getSimpleName());
        out.print("(");
        Type[] arguments = someConstructor.getGenericParameterTypes();
        if (arguments.length > 0 && arguments[0].getTypeName().equals(outerClass.getTypeName())) {
            arguments[0] = null;
        }
        printArguments(someConstructor.getDeclaringClass().getPackage(), arguments, out);
        out.print(")");
        printExceptions(someConstructor.getDeclaringClass().getPackage(), someConstructor.getExceptionTypes(), out);
        out.println(" {");
    }

    private static void printConstructors(String indent,
                                     Class<?> someClass, Class<?> outerClass,
                                     PrintWriter out) throws IOException {
        for (Constructor constructor : someClass.getDeclaredConstructors()) {
            if (!constructor.isSynthetic()) {
                printConstructorHeader(indent, constructor, outerClass, out);
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
            printClass(indent, clazz, Modifier.isStatic(clazz.getModifiers()) ? null : someClass, out);
        }
    }

    private static void printTypeParameters(Package currentPackage,
                                            TypeVariable[] types,
                                            PrintWriter out) throws IOException {
        if (types.length > 0) {
            out.print(Arrays.stream(types)
                    .map(t -> {
                        String result = getShortTypeName(currentPackage, t);
                        List<String> bounds = Arrays.stream(t.getBounds())
                                .map(b -> getShortTypeName(currentPackage, b))
                                .filter(s -> !s.equals("java.lang.Object"))
                                .collect(Collectors.toList());
                        if (!bounds.isEmpty()) {
                            result += bounds.stream().collect(Collectors.joining(" & ", " extends ", ""));
                        }
                        return result;
                    })
                    .collect(Collectors.joining(", ", "<", "")) + ">");
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
        if (!someClass.isInterface()) {
            out.print("class ");
        }
        out.print(someClass.getSimpleName());
        printTypeParameters(someClass.getPackage(), someClass.getTypeParameters(), out);
        Class<?> superclass = someClass.getSuperclass();
        if (superclass != Object.class && superclass != null) {
            out.print(" extends");
            out.print(" " + getShortTypeName(someClass.getPackage(), someClass.getGenericSuperclass()));
        }
        Type[] interfaces = someClass.getGenericInterfaces();
        if (interfaces.length > 0) {
            out.print(" implements");
            out.print(Arrays.stream(interfaces)
                    .map(t -> getShortTypeName(someClass.getPackage(), t))
                    .collect(Collectors.joining(", ", " ", "")));
        }
        out.println(" {");
        out.println();
    }

    private static void printClass(String indent,
                                   Class<?> someClass,
                                   Class<?> outerClass,
                                   PrintWriter out) throws IOException {
        if (!someClass.isSynthetic()) {
            printClassHeader(indent, someClass, out);
            printFields(indent + "    ", someClass, out);
            printConstructors(indent + "    ", someClass, outerClass, out);
            printMethods(indent + "    ", someClass, out);
            printInnerAndNestedClasses(indent + "    ", someClass, out);
            out.println(indent + "}");
            out.println();
        }
    }

    public static void printStructure(Class<?> someClass) {
        try (PrintWriter out = new PrintWriter(someClass.getSimpleName() + ".java")) {
            out.println("package " + someClass.getPackage().getName() + ";\n");
            printClass("", someClass, null, out);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
