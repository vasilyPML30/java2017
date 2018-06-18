package net.netau.vasyoid.annotations;

import java.lang.annotation.*;

/**
 * Methods annotated as @AfterClass will be executed after all test methods.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterClass { }