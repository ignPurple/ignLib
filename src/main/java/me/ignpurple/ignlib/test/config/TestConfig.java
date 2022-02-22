package me.ignpurple.ignlib.test.config;

import me.ignpurple.ignlib.configuration.Configuration;
import me.ignpurple.ignlib.configuration.annotation.ConfigurationField;
import me.ignpurple.ignlib.enums.ConfigType;
import org.bukkit.plugin.java.JavaPlugin;

public class TestConfig extends Configuration {

    @ConfigurationField(path = "test")
    private String testA = "This is a test string!";

    @ConfigurationField(path = "abc.test")
    private int testB = 1;

    public TestConfig(JavaPlugin plugin) {
        super(plugin.getDataFolder().toPath(), ConfigType.YAML, "test");
    }

    public String getTestA() {
        return this.testA;
    }

    public int getTestB() {
        return this.testB;
    }
}
