package me.ignpurple.ignlib.configuration.adapter;

import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;

public class DefaultFieldLoader implements CustomFieldLoader {

    @Override
    public Object deserialize(ConfigurationManager configurationManager, Object fieldValue, Object object) {
        return object;
    }

    @Override
    public Object serialize(ConfigurationManager configurationManager, Object object) {
        return object;
    }
}
