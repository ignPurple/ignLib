package me.ignpurple.ignlib.configuration.type;

import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;

public interface ConfigLoader {

    void save();

    Object getOrCreate(ConfigurationField configurationField, Object fieldValue);
}
