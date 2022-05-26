package me.ignpurple.ignlib.configuration.annotation;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.adapter.DefaultFieldLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationField {

    /**
     * @return The field in the configuration where the data will be stored
     */
    String path();

    Class<? extends CustomFieldLoader> fieldLoader() default DefaultFieldLoader.class;
}
