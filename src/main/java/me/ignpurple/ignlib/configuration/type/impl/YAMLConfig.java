package me.ignpurple.ignlib.configuration.type.impl;

import me.ignpurple.ignlib.configuration.Configuration;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.configuration.type.ConfigLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YAMLConfig implements ConfigLoader {
    private final YamlConfiguration yamlConfig;
    private final File file;

    public YAMLConfig(Configuration configuration) {
        final File file = configuration.getDataFolder().toFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        this.yamlConfig = new YamlConfiguration();
        this.file = new File(file, configuration.getConfigName() + ".yml");

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
    public Object getOrCreate(ConfigurationField configurationField, Object fieldValue) {
        final String path = configurationField.path();
        if (this.yamlConfig.contains(configurationField.path())) {
            return this.yamlConfig.get(path);
        }

        this.yamlConfig.set(path, fieldValue);
        return fieldValue;
    }
}
