package me.ignpurple.ignlib.configuration.type.impl;

import me.ignpurple.ignlib.configuration.Configuration;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.loader.CustomFieldLoader;
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

        final File file = configuration.getDataFolder().toFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        this.load();
    }

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

    @Override
    public void save() {
        try {
            this.yamlConfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getOrCreate(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, Object fieldValue) {
        final String path = configurationField.path();
        if (this.yamlConfig.contains(configurationField.path())) {
            final Object configValue = this.yamlConfig.get(path);
            return customFieldLoader == null ? configValue : customFieldLoader.load(configValue);
        }

        final Object loadedObject = customFieldLoader == null ? fieldValue : customFieldLoader.save(fieldValue);
        this.yamlConfig.set(path, loadedObject);
        return fieldValue;
    }

    @Override
    public void set(CustomFieldLoader customFieldLoader, ConfigurationField configurationField, Object fieldValue) {
        final String path = configurationField.path();
        final Object loadedObject = customFieldLoader == null ? fieldValue : customFieldLoader.save(fieldValue);
        this.yamlConfig.set(path, loadedObject);
    }
}
