package me.ignpurple.ignlib.configuration.type;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.field.ObjectField;

public interface ConfigLoader {

    void load();

    void save();

    Object getOrCreate(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, ObjectField fieldValue);

    void set(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, Object fieldValue);
}
