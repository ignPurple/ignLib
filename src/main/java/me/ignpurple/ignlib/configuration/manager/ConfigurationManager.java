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

    public <T extends Configuration> T getConfig(Class<T> configuration) {
        return (T) this.configurations.get(configuration);
    }

    public void registerConfiguration(Configuration configuration) {
        this.configurations.put(configuration.getClass(), configuration);
    }

    public void loadAll() {
        for (final Configuration configuration : this.configurations.values()) {
            configuration.load();
        }
    }

    public CustomFieldLoader getLoader(Class<?> type) {
        return this.typeAdapters.get(ConfigField.create(type));
    }

    public void registerTypeAdapater(Class<?> object, CustomFieldLoader customFieldLoader) {
        this.typeAdapters.put(ConfigField.create(object), customFieldLoader);
    }
}
