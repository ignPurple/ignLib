package me.ignpurple.ignlib.configuration.annotation;

import me.ignpurple.ignlib.configuration.loader.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.loader.DefaultFieldLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationField {

    String path();

    Class<? extends CustomFieldLoader> loader() default DefaultFieldLoader.class;
}
