package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implements a functions to print the interior of a class and to compare two classes
 */
public class Reflector {

    @NotNull
    private static String getShortTypeName(@NotNull Package currentPackage,
                                           @NotNull Type type) {
        String result = type.getTypeName();
        if (!result.startsWith(currentPackage.getName())) {
            return result;
        }
        return result.replaceAll(currentPackage.getName() + ".", "");
    }

    private static void printField(@NotNull String indent,
                                   @NotNull Field someField,
                                   @NotNull Writer out) throws IOException {
        out.write(indent);
        String modifiers = Modifier.toString(someField.getModifiers());
        if (!modifiers.equals("")) {
            out.write(modifiers + " ");
        }
        out.write(getShortTypeName(someField.getDeclaringClass().getPackage(),
                someField.getGenericType()));
        out.write(" " + someField.getName());
        if (Modifier.isFinal(someField.getModifiers())) {
            out.write(" = " + getDefaultValue(someField.getType()));
        }
        out.write(";\n\n");

    }

        private static void printFields(@NotNull String indent,
                                        @NotNull Class<?> someClass,
                                        @NotNull Writer out) throws IOException {
        Field[] fields = someClass.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (Field field : fields) {
            if (!field.isSynthetic()) {
                printField(indent, field, out);
            }
        }
    }

    private static void printExceptions(@NotNull Package currentPackage,
                                        @NotNull Type[] exceptions,
                                        @NotNull Writer out) throws IOException {
        if (exceptions.length > 0) {
            out.write(" throws");
            out.write(Arrays.stream(exceptions)
                    .map(t -> getShortTypeName(currentPackage, t))
                    .collect(Collectors.joining(", ", " ", "")));
        }
    }

    private static void printArguments(@NotNull Package currentPackage,
                                       @Nullable Type @NotNull [] arguments,
                                       @NotNull Writer out) throws IOException {

        out.write(Arrays.stream(arguments)
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

    private static void printMethodHeader(@NotNull String indent,
                                          @NotNull Method someMethod,
                                          @NotNull Writer out) throws IOException {
        out.write(indent);
        String modifiers = Modifier.toString(someMethod.getModifiers());
        if (!modifiers.equals("")) {
            out.write(modifiers + " ");
        }
        printTypeParameters(someMethod.getDeclaringClass().getPackage(), someMethod.getTypeParameters(), out);
        if (someMethod.getTypeParameters().length > 0) {
            out.write(" ");
        }
        out.write(getShortTypeName(someMethod.getDeclaringClass().getPackage(), someMethod.getGenericReturnType()) + " ");
        out.write(someMethod.getName());
        out.write("(");


        printArguments(someMethod.getDeclaringClass().getPackage(), someMethod.getGenericParameterTypes(), out);
        out.write(")");
        printExceptions(someMethod.getDeclaringClass().getPackage(), someMethod.getGenericExceptionTypes(), out);
    }

    private static void printConstructorHeader(@NotNull String indent,
                                               @NotNull Constructor someConstructor,
                                               @Nullable Class<?> outerClass,
                                               @NotNull Writer out) throws IOException {
        out.write(indent);
        String modifiers = Modifier.toString(someConstructor.getModifiers());
        if (!modifiers.equals("")) {
            out.write(modifiers + " ");
        }
        printTypeParameters(someConstructor.getDeclaringClass().getPackage(), someConstructor.getTypeParameters(), out);
        if (someConstructor.getTypeParameters().length > 0) {
            out.write(" ");
        }
        out.write(someConstructor.getDeclaringClass().getSimpleName());
        out.write("(");
        Type[] arguments = someConstructor.getGenericParameterTypes();
        if (arguments.length > 0 && outerClass != null && arguments[0].getTypeName().equals(outerClass.getTypeName())) {
            arguments[0] = null;
        }
        printArguments(someConstructor.getDeclaringClass().getPackage(), arguments, out);
        out.write(")");
        printExceptions(someConstructor.getDeclaringClass().getPackage(), someConstructor.getExceptionTypes(), out);
    }

    private static void printConstructors(@NotNull String indent,
                                          @NotNull Class<?> someClass,
                                          @Nullable Class<?> outerClass,
                                          @NotNull Writer out) throws IOException {
        Constructor<?>[] constructors = someClass.getDeclaredConstructors();
        Arrays.sort(constructors, Comparator.comparing((Constructor<?> c) -> {
            StringWriter sw = new StringWriter();
            try {
                printConstructorHeader("", c, outerClass, sw);
            } catch (Exception ignored) {}
            return sw.toString();
        }));
        for (Constructor constructor : constructors) {
            if (!constructor.isSynthetic()) {
                printConstructorHeader(indent, constructor, outerClass, out);
                out.write(" {\n");
                out.write(indent + "}\n\n");
            }
        }
    }

    private static String getDefaultValue(@NotNull Class<?> type) {
        if (type == boolean.class) {
            return "false";
        } else if (type.isPrimitive()) {
            return"0";
        }
        return "null";
    }

    private static void printMethods(@NotNull String indent,
                                     @NotNull Class<?> someClass,
                                     @NotNull Writer out) throws IOException {
        Method[] methods = someClass.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(m -> {
            StringWriter sw = new StringWriter();
            try {
                printMethodHeader("", m, sw);
            } catch (Exception ignored) {}
            return m.getName() + m.getGenericReturnType().getTypeName() + sw.toString();
        }));
        for (Method method : methods) {
            if (!method.isSynthetic()) {
                printMethodHeader(indent, method, out);
                if (Modifier.isAbstract(method.getModifiers())) {
                    out.write(";\n\n");
                    continue;
                }
                out.write(" {\n");
                Class returnType = method.getReturnType();
                if (returnType == void.class) {
                    out.write("\n");
                } else {
                    out.write(indent + "    " + "return ");
                    out.write(getDefaultValue(returnType));
                    out.write(";\n");
                }
                out.write(indent + "}\n\n");
            }
        }
    }

    private static void printInnerAndNestedClasses(@NotNull String indent,
                                                   @NotNull Class<?> someClass,
                                                   @NotNull Writer out) throws IOException {
        Class<?>[] classes = someClass.getDeclaredClasses();
        Arrays.sort(classes, Comparator.comparing(Class::getName));
        for (Class<?> clazz : classes) {
            printClass(indent, clazz, Modifier.isStatic(clazz.getModifiers()) ? null : someClass, out);
        }
    }

    private static void printTypeParameters(@NotNull Package currentPackage,
                                            @NotNull TypeVariable[] types,
                                            @NotNull Writer out) throws IOException {
        if (types.length > 0) {
            out.write(Arrays.stream(types)
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

    private static void printClassHeader(@NotNull String indent,
                                         @NotNull Class<?> someClass,
                                         @NotNull Writer out) throws IOException {
        out.write(indent);
        String modifiers = Modifier.toString(someClass.getModifiers());
        if (!modifiers.equals("")) {
            out.write(modifiers + " ");
        }
        if (!someClass.isInterface()) {
            out.write("class ");
        }
        out.write(someClass.getSimpleName());
        printTypeParameters(someClass.getPackage(), someClass.getTypeParameters(), out);
        Class<?> superclass = someClass.getSuperclass();
        if (superclass != Object.class && superclass != null) {
            out.write(" extends");
            out.write(" " + getShortTypeName(someClass.getPackage(), someClass.getGenericSuperclass()));
        }
        Type[] interfaces = someClass.getGenericInterfaces();
        if (interfaces.length > 0) {
            if (someClass.isInterface()) {
                out.write(" extends");
            } else {
                out.write(" implements");
            }
            out.write(Arrays.stream(interfaces)
                    .map(t -> getShortTypeName(someClass.getPackage(), t))
                    .collect(Collectors.joining(", ", " ", "")));
        }
    }

    private static void printClass(@NotNull String indent,
                                   @NotNull Class<?> someClass,
                                   @Nullable Class<?> outerClass,
                                   @NotNull Writer out) throws IOException {
        if (!someClass.isSynthetic()) {
            printClassHeader(indent, someClass, out);
            out.write(" {\n\n");
            printFields(indent + "    ", someClass, out);
            printConstructors(indent + "    ", someClass, outerClass, out);
            printMethods(indent + "    ", someClass, out);
            printInnerAndNestedClasses(indent + "    ", someClass, out);
            out.write(indent + "}\n\n");
        }
    }

    /**
     * Gets a class and prints its structure into a file with the corresponding name.
     * The output file is guaranteed to be successfully
     * compilable if put into the same package as the given class.
     * Java coding conventions are followed.
     * @param someClass class to print
     */
    public static void printStructure(@NotNull Class<?> someClass) {
        try (Writer out = new PrintWriter(someClass.getSimpleName() + ".java")) {
            printStructure(someClass, out);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The same as void printStructure(Class<?>) but additionally takes an outputStream
     * @param someClass class to print
     * @throws IOException when an output error occurs
     */
    public static void printStructure(@NotNull Class<?> someClass,
                                      @NotNull Writer outputStream) throws IOException {
        outputStream.write("package " + someClass.getPackage().getName() + ";\n\n");
        printClass("", someClass, null, outputStream);
    }


    @NotNull
    private static <T> List<T> symmetricDifference(@NotNull List<T> a,
                                                   @NotNull List<T> b,
                                                   @NotNull BiConsumer<T, Writer> printItem) {
        BiPredicate<T, T> equal = (c1, c2) -> {
            StringWriter s1 = new StringWriter();
            StringWriter s2 = new StringWriter();
            try {
                printItem.accept(c1, s1);
                printItem.accept(c2, s2);
            } catch (Exception e) {
                return false;
            }
            return s1.toString().equals(s2.toString());
        };
        List<T> result = new ArrayList<>(a);
        for (T x : b) {
            result.add(x);
            for (T y : a) {
                if (equal.test(x, y)) {
                    result.remove(x);
                    result.remove(y);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Compares fields and methods of two classes ad prints the difference
     *
     * Two fields are equal when they have the same generic type and name
     * (e.g. <code>class Class1&lt;T&gt; { T x; }</code> is not equal to
     * <code>class Class2&lt;E&gt; { E x; }</code>)
     *
     * Two constructors are equal when they have the same set of argument types
     * (e.g. <code>Class1(String s);</code> is equal to <code>Class2(String s);</code>)
     * or if they take argument of the opposite class
     * (e.g. <code>Class1(Class2 c);</code> is equal to <code>Class2(Class1 c);</code>)
     * or of the declaring class
     * (e.g. <code>Class1(Class1 c);</code> is equal to <code>Class2(Class2 c);</code>)
     *
     * Two methods are equal when they have the same name and set of argument types
     *
     * @param a first compared class
     * @param b second compared class
     * @return true if the classes are equal, false otherwise
     */
    public static boolean diffClasses(@NotNull Class<?> a,
                                      @NotNull Class<?> b) {
        List<Constructor> constructors = symmetricDifference(
                Arrays.asList(a.getDeclaredConstructors()),
                Arrays.asList(b.getDeclaredConstructors()),
                (c, w) -> {
                    try {
                        Writer tmpWriter = new StringWriter();
                        printConstructorHeader("", c, null, tmpWriter);
                        if (c.getDeclaringClass().equals(a)) {
                            w.write(tmpWriter
                                    .toString()
                                    .replace(a.getSimpleName(), "%1%")
                                    .replace(b.getSimpleName(), "%2%"));
                        } else {
                            w.write(tmpWriter
                                    .toString()
                                    .replace(b.getSimpleName(), "%1%")
                                    .replace(a.getSimpleName(), "%2%"));
                        }
                    } catch (Exception ignored) {}
                });
        List<Method> methods = symmetricDifference(
                Arrays.asList(a.getDeclaredMethods()),
                Arrays.asList(b.getDeclaredMethods()),
                (m, w) -> {
                    try {
                        printMethodHeader("", m, w);
                    } catch (Exception ignored) {}
                });
        List<Field> fields = symmetricDifference(
                Arrays.asList(a.getDeclaredFields()),
                Arrays.asList(b.getDeclaredFields()),
                (f, w) -> {
                    try {
                        printField("", f, w);
                    } catch (Exception ignored) {}
                });
        try {
            PrintWriter sOut = new PrintWriter(System.out);
            for (Field field : fields) {
                sOut.write("/* " + field.getDeclaringClass().getName() + " */\n");
                printField("", field, sOut);
            }
            for (Constructor constructor : constructors) {
                sOut.write("/* " + constructor.getDeclaringClass().getName() + " */\n");
                printConstructorHeader("", constructor, null, sOut);
                sOut.write(";\n\n");
            }
            for (Method method : methods) {
                sOut.write("/* " + method.getDeclaringClass().getName() + " */\n");
                printMethodHeader("", method, sOut);
                sOut.write(";\n\n");
            }
            sOut.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return constructors.isEmpty() && methods.isEmpty() && fields.isEmpty();
    }
}
