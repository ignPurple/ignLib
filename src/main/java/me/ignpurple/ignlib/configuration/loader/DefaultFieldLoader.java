package me.ignpurple.ignlib.configuration.loader;

public class DefaultFieldLoader implements CustomFieldLoader {

    @Override
    public Object load(Object object) {
        return object;
    }

    @Override
    public Object save(Object object) {
        return object;
    }
}
