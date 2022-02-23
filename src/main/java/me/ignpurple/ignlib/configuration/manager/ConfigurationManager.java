package me.ignpurple.ignlib.configuration.manager;

import me.ignpurple.ignlib.configuration.Configuration;
import me.ignpurple.ignlib.configuration.loader.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.loader.DefaultFieldLoader;
import me.ignpurple.ignlib.configuration.type.ConfigField;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class ConfigurationManager {
    private final Map<Class<? extends Configuration>, Configuration> configurations;
    private final Map<ConfigField<?>, CustomFieldLoader> typeAdapters;

    private static final DefaultFieldLoader DEFAULT_FIELD_LOADER = new DefaultFieldLoader();

    public ConfigurationManager() {
        this.configurations = new IdentityHashMap<>();
        this.typeAdapters = new HashMap<>();
    }

    /**
     * Get a configuration file from the class assigned to it
     *
     * @param configuration The class to get the config by
     * @return The configuration class
     */
    public <T extends Configuration> T getConfig(Class<T> configuration) {
        return (T) this.configurations.get(configuration);
    }

    /**
     * Register a configuration into memory
     *
     * @param configuration The configuration to cache
     */
    public void registerConfiguration(Configuration configuration) {
        this.configurations.put(configuration.getClass(), configuration);
    }

    /**
     * Load all the fields from every configuration file
     */
    public void loadAll() {
        for (final Configuration configuration : this.configurations.values()) {
            configuration.load();
        }
    }

    /**
     * Get a loader depending on the object
     *
     * @param type The object to get the loader for
     * @return The loader to use for the object
     */
    public CustomFieldLoader getLoader(Class<?> type) {
        return this.typeAdapters.get(ConfigField.create(type));
    }

    /**
     * Register a {@link CustomFieldLoader} into memory
     *
     * @param object            The object that needs to be converted
     * @param customFieldLoader The loader to use for converting the object
     */
    public void registerTypeAdapater(Class<?> object, CustomFieldLoader customFieldLoader) {
        this.typeAdapters.put(ConfigField.create(object), customFieldLoader);
    }
}
