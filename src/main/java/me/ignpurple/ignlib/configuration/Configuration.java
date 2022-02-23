package me.ignpurple.ignlib.configuration;

import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.loader.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;
import me.ignpurple.ignlib.configuration.type.ConfigLoader;
import me.ignpurple.ignlib.enums.ConfigType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public abstract class Configuration {
    private ConfigurationManager configurationManager;
    private ConfigLoader configLoader;

    private final String configName;
    private final Path dataFolder;

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

    public String getConfigName() {
        return this.configName;
    }

    public Path getDataFolder() {
        return this.dataFolder;
    }

    public <T> void load() {
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

    public void reload() {
        this.configLoader.load();
        this.load();
    }
}
