package me.ignpurple.ignlib.configuration.adapter.defaults;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.field.ObjectField;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldAdapter implements CustomFieldLoader {

    @Override
    public Object serialize(ConfigurationManager configurationManager, Object object) {
        final World world = (World) object;
        return world.getName();
    }

    @Override
    public Object deserialize(ConfigurationManager configurationManager, ObjectField fieldValue, Object object) {
        final String worldName = (String) object;
        try {
            final World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new IllegalStateException("Invalid World Specified! Consider adding Multiverse-Core as a soft-depend if you have it installed!");
            }

            return Bukkit.getWorld(worldName);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
