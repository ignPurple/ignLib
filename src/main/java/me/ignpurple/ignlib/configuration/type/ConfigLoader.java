package me.ignpurple.ignlib.configuration.type;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;

public interface ConfigLoader {

    void load();

    void save();

    Object getOrCreate(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, Object fieldValue);

    void set(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, Object fieldValue);
}
