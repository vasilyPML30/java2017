package net.netau.vasyoid.annotations;

import java.lang.annotation.*;

/**
 * Methods annotated as @Before will be executed before each test method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Before { }