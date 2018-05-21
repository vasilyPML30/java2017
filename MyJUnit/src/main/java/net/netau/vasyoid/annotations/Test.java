package net.netau.vasyoid.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    Class<?> expected() default None.class;
    String ignore() default "";

    class None { }

}