package net.netau.vasyoid.annotations;

import java.lang.annotation.*;

/**
 * Methods annotated as @BeforeClass will be executed before all test methods.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeClass { }