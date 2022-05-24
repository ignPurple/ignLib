package me.ignpurple.ignlib.configuration;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;
import me.ignpurple.ignlib.configuration.type.ConfigLoader;
import me.ignpurple.ignlib.enums.ConfigType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public abstract class Configuration {
    private final String configName;
    private final Path dataFolder;

    private ConfigurationManager configurationManager;
    private ConfigLoader configLoader;

    public Configuration(ConfigurationManager configurationManager, Path path, ConfigType configType, String configName) {
        this.configurationManager = configurationManager;

        this.configName = configName;
        this.dataFolder = path;

        try {
            this.configLoader = configType.getConfigLoader().getDeclaredConstructor(Configuration.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.getCause().printStackTrace();
            e.printStackTrace();
        }
    }

    /**
     * Get the name of the configuration file
     *
     * @return The name of the file
     */
    public String getConfigName() {
        return this.configName;
    }

    /**
     * Gets the folder where the configuration file will be stored
     *
     * @return The path of the folder
     */
    public Path getDataFolder() {
        return this.dataFolder;
    }

    /**
     * Gets the configuration manager
     *
     * @return The configuration manager instance
     */
    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    /**
     * Load all the data from the configuration file with
     * class fields which have the {@link ConfigurationField} annotation
     */
    public void load() {
        try {
            for (final Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (!field.isAnnotationPresent(ConfigurationField.class)) {
                    continue;
                }

                final Object fieldValue = field.get(this);

                final ConfigurationField configurationField = field.getAnnotation(ConfigurationField.class);
                final CustomFieldLoader customFieldLoader = this.configurationManager.getLoader(field.getType());
                final Object newDeclaration = this.configLoader.getOrCreate(customFieldLoader, configurationField, fieldValue);
                field.set(this, newDeclaration);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            this.configLoader.save();
        }
    }

    /**
     * Saves all the data to the configuration file with
     * class fields which have the {@link ConfigurationField} annotation
     */
    public void save() {
        try {
            for (final Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (!field.isAnnotationPresent(ConfigurationField.class)) {
                    continue;
                }

                final Object fieldValue = field.get(this);

                final ConfigurationField configurationField = field.getAnnotation(ConfigurationField.class);
                final CustomFieldLoader customFieldLoader = this.configurationManager.getLoader(field.getType());
                this.configLoader.set(customFieldLoader, configurationField, fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            this.configLoader.save();
        }
    }

    /**
     * Re-loads the data from the configuration file
     */
    public void reload() {
        this.configLoader.load();
        this.load();
    }
}
