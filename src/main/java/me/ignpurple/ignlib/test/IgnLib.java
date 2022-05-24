package me.ignpurple.ignlib.test;

import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;
import me.ignpurple.ignlib.test.config.TestConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class IgnLib extends JavaPlugin {
    private static final boolean TEST = true;

    @Override
    public void onEnable() {
        if (!TEST) {
            return;
        }

        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.registerConfiguration(new TestConfig(this, configurationManager));
        configurationManager.loadAll();

        final TestConfig testConfig = configurationManager.getConfig(TestConfig.class);
    }
}
