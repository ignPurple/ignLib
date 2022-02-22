package me.ignpurple.ignlib.enums;

import me.ignpurple.ignlib.configuration.type.ConfigLoader;
import me.ignpurple.ignlib.configuration.type.impl.YAMLConfig;

public enum ConfigType {

    YAML(YAMLConfig.class);

    private final Class<? extends ConfigLoader> configLoader;

    ConfigType(Class<? extends ConfigLoader> configLoader) {
        this.configLoader = configLoader;
    }

    public Class<? extends ConfigLoader> getConfigLoader() {
        return this.configLoader;
    }
}
