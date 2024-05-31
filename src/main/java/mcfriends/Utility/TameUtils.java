package mcfriends.Utility;

import com.destroystokyo.paper.entity.Pathfinder;
import mcfriends.mcfriends.FoxListener;
import mcfriends.mcfriends.MCFriends;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TameUtils {
    private static Plugin plugin;

    public static void initialize(Plugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static boolean tameMob(Entity entity, Player player, Material requiredItem) {
        // Check if the entity is already tamed
        if (entity.hasMetadata("tamed")) {
            player.sendMessage("This animal is already tamed by " + player.getName());
            return false; // Mob is already tamed
        }

        // Check if the player has the required item in hand
        if (player.getInventory().getItemInMainHand().getType() != requiredItem) {
            player.sendMessage("You need " + requiredItem.name() + " to tame this mob");
            return false; // Player doesn't have the required item
        }

        // Tame the mob
        entity.setMetadata("tamed", new FixedMetadataValue(plugin, true));
        // Additional logic can be added here, e.g., changing mob behavior

        // Remove the required item from the player's hand
        ItemStack requiredItemStack = new ItemStack(requiredItem);
        player.getInventory().removeItem(requiredItemStack);

        return true; // Taming successful
    }

    private static Map<Player, Mob> tamedMobs = new HashMap<>();

    public static boolean tameMobs(Mob mob, Player player) {
        // Tame the mob and add it to the map of tamed mobs
        tamedMobs.put(player, mob);
        startFollowingTask(player, mob);
        return true; // Taming successful
    }

    private static void startFollowingTask(Player player, Mob mob) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!tamedMobs.containsKey(player) || !tamedMobs.get(player).isValid() || !player.isValid()) {
                    this.cancel(); // Stop following if player or mob is no longer valid
                    return;
                }

                Location targetLocation = player.getLocation();
                mob.getPathfinder().moveTo(targetLocation, 1.0);

                PanicAI panicAI = new PanicAI(mob);
                panicAI.setTamed(true);

            }
        }
        .runTaskTimer(JavaPlugin.getPlugin(MCFriends.class), 0L, 20L); // Adjust the delay and interval as needed
    }

    public static boolean isCarriedByPlayer(Player player, Fox fox) {
        // Check if the fox has the flag "isBeingCarriedByPlayer" set to true
        //return fox.isBeingCarriedByPlayer();
        return false;
    }
}