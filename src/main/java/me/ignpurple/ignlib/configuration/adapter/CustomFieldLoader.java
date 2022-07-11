package me.ignpurple.ignlib.configuration.adapter;

import me.ignpurple.ignlib.configuration.field.ObjectField;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;

public interface CustomFieldLoader<T> {

    /**
     * When the object is saved to the configuration file
     *
     * @param object The object stored in memory
     * @return The new converted object to save to the configuration
     */
    Object serialize(ConfigurationManager configurationManager, T object);

    /**
     * When the object is loaded from the configuration file
     *
     * @param object The object in the configuration
     * @return The new converted object to load into memory
     */
    T deserialize(ConfigurationManager configurationManager, ObjectField fieldValue, Object object);

}
