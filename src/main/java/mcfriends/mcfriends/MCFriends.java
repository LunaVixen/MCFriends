package mcfriends.mcfriends;

import mcfriends.Utility.TameUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCFriends extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MCFriends is starting up!");
        getServer().getPluginManager().registerEvents(new FoxListener(), this);

        TameUtils.initialize(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MCFriends is powering down!");
    }
}
