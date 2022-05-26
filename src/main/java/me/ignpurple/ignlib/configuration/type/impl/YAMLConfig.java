package me.ignpurple.ignlib.configuration.type.impl;

import me.ignpurple.ignlib.configuration.Configuration;
import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.field.ObjectField;
import me.ignpurple.ignlib.configuration.type.ConfigLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YAMLConfig implements ConfigLoader {
    private final Configuration configuration;

    private YamlConfiguration yamlConfig;
    private File file;

    public YAMLConfig(Configuration configuration) {
        this.configuration = configuration;

        this.file = configuration.getDataFolder().toFile();
        if (!this.file.exists()) {
            this.file.mkdirs();
        }

        this.load();
    }

    /**
     * Load the Configuration into memory from the file
     */
    @Override
    public void load() {
        this.yamlConfig = new YamlConfiguration();
        this.file = new File(this.file, this.configuration.getConfigName() + ".yml");

        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }

            this.yamlConfig.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the configuration file
     */
    @Override
    public void save() {
        try {
            this.yamlConfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a field from a configuration file using a {@link CustomFieldLoader} if present
     *
     * @param customFieldLoader  The loader to use to load the field from the configuration
     * @param configurationField The field annotation to use for getting the path
     * @param fieldValue         The value to set in the config if it isn't present
     * @return The value from the configuration file
     */
    @Override
    public Object getOrCreate(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, ObjectField fieldValue) {
        final String path = configurationField.path();
        if (this.yamlConfig.contains(configurationField.path())) {
            final Object configValue = this.yamlConfig.get(path);
            return customFieldLoader == null ? configValue : customFieldLoader.deserialize(this.configuration.getConfigurationManager(), fieldValue, configValue);
        }

        final Object loadedObject = customFieldLoader == null ? fieldValue.getFieldObject() : customFieldLoader.serialize(this.configuration.getConfigurationManager(), fieldValue.getFieldObject());
        this.yamlConfig.set(path, loadedObject);
        return fieldValue;
    }

    /**
     * Sets a field inside a configuration file using a {@link CustomFieldLoader} if present
     *
     * @param customFieldLoader  The loader to use to save the field to the configuration
     * @param configurationField The field annotation to use for getting the path
     * @param fieldValue         The value to set in the configuration
     */
    @Override
    public void set(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, Object fieldValue) {
        final String path = configurationField.path();
        final Object loadedObject = customFieldLoader == null ? fieldValue : customFieldLoader.serialize(this.configuration.getConfigurationManager(), fieldValue);
        this.yamlConfig.set(path, loadedObject);
    }
}
