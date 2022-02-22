package me.ignpurple.ignlib.configuration;

import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.loader.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.loader.DefaultFieldLoader;
import me.ignpurple.ignlib.configuration.type.ConfigLoader;
import me.ignpurple.ignlib.enums.ConfigType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public abstract class Configuration {
    private ConfigLoader configLoader;

    private final String configName;
    private final Path dataFolder;

    public Configuration(Path path, ConfigType configType, String configName) {
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
                if (!field.isAnnotationPresent(ConfigurationField.class)) {
                    continue;
                }

                final ConfigurationField configurationField = field.getAnnotation(ConfigurationField.class);
                final Class<? extends CustomFieldLoader> customFieldLoader = configurationField.loader();
                field.setAccessible(true);

                final Object fieldValue = field.get(this);

                if (customFieldLoader == DefaultFieldLoader.class) {
                    field.set(this, this.configLoader.getOrCreate(configurationField, fieldValue));
                    continue;
                }

                final CustomFieldLoader customFieldLoaderInstance = customFieldLoader.newInstance();
                final Object newDeclaration = customFieldLoaderInstance.load(this.configLoader.getOrCreate(configurationField, fieldValue));
                field.set(this, newDeclaration);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            this.configLoader.save();
        }
    }
}
