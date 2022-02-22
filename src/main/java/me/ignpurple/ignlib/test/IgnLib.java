package me.ignpurple.ignlib.test;

import me.ignpurple.ignlib.test.config.TestConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class IgnLib extends JavaPlugin {
    private static final boolean TEST = false;

    @Override
    public void onEnable() {
        if (!TEST) {
            return;
        }

        final TestConfig testConfig = new TestConfig(this);
        testConfig.load();

        System.out.println(testConfig.getTestA());
        System.out.println(testConfig.getTestB());
    }
}
