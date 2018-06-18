package net.netau.vasyoid.annotations;

import java.lang.annotation.*;

/**
 * Methods annotated as @After will be executed after each test method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface After { }