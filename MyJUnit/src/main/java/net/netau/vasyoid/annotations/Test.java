package net.netau.vasyoid.annotations;

import java.lang.annotation.*;

/**
 * Methods annotated as @Test will be executed as tests.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    /**
     * Specifies an exception type that is expected to be thrown while executing a test.
     * @return exception class.
     */
    Class<?> expected() default None.class;

    /**
     * Specifies an reason why a test must be ignored.
     * If the reason is empty, the test will be executed.
     * @return exception class.
     */
    String ignore() default "";

    class None { }

}