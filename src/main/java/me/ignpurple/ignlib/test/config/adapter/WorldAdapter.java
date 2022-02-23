package me.ignpurple.ignlib.test.config.adapter;

import me.ignpurple.ignlib.configuration.loader.CustomFieldLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldAdapter implements CustomFieldLoader {

    @Override
    public Object save(Object object) {
        final World world = (World) object;
        return world.getName();
    }

    @Override
    public Object load(Object object) {
        final String worldName = (String) object;
        return Bukkit.getWorld(worldName);
    }
}
