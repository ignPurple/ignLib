package me.ignpurple.ignlib.test;

import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;
import me.ignpurple.ignlib.test.config.TestConfig;
import me.ignpurple.ignlib.test.config.adapter.WorldAdapter;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class IgnLib extends JavaPlugin {
    private static final boolean TEST = false;

    @Override
    public void onEnable() {
        if (!TEST) {
            return;
        }

        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.registerTypeAdapater(World.class, new WorldAdapter());
        configurationManager.registerConfiguration(new TestConfig(this, configurationManager));
        configurationManager.loadAll();

        final TestConfig testConfig = configurationManager.getConfig(TestConfig.class);
        System.out.println(testConfig.getWorld().getName());
    }
}
