package me.ignpurple.ignlib.configuration.adapter;

import me.ignpurple.ignlib.configuration.field.ObjectField;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;

public interface CustomFieldLoader {

    /**
     * When the object is saved to the configuration file
     *
     * @param object The object stored in memory
     * @return The new converted object to save to the configuration
     */
    Object serialize(ConfigurationManager configurationManager, Object object);

    /**
     * When the object is loaded from the configuration file
     *
     * @param object The object in the configuration
     * @return The new converted object to load into memory
     */
    Object deserialize(ConfigurationManager configurationManager, ObjectField fieldValue, Object object);

}
