package me.ignpurple.ignlib.configuration.manager;

import me.ignpurple.ignlib.configuration.Configuration;
import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.adapter.DefaultFieldLoader;
import me.ignpurple.ignlib.configuration.adapter.defaults.ListAdapter;
import me.ignpurple.ignlib.configuration.adapter.defaults.WorldAdapter;
import me.ignpurple.ignlib.configuration.type.ConfigField;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigurationManager {
    private final Plugin plugin;
    private final Map<Class<? extends Configuration>, Configuration> configurations;
    private final Map<ConfigField<?>, CustomFieldLoader> typeAdapters;

    public static final DefaultFieldLoader DEFAULT_FIELD_LOADER = new DefaultFieldLoader();

    public ConfigurationManager(Plugin plugin) {
        this.plugin = plugin;
        this.configurations = new IdentityHashMap<>();
        this.typeAdapters = new HashMap<>();

        this.registerTypeAdapater(List.class, new ListAdapter());
        this.registerTypeAdapater(ArrayList.class, new ListAdapter());
        this.registerTypeAdapater(World.class, new WorldAdapter());
    }

    /**
     * Saves a File from the Plugin jar or creates it if it doesn't exist
     *
     * @param fileName The name of the file to copy from the Plugin Jar
     */
    public File copyOrCreate(String fileName) {
        final File file = new File(this.plugin.getDataFolder(), fileName);
        if (file.exists()) {
            return file;
        }

        try {
            this.plugin.saveResource(fileName, false);
        } catch (Exception exception) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
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
        return this.typeAdapters.getOrDefault(ConfigField.create(type), DEFAULT_FIELD_LOADER);
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
