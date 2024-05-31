package mcfriends.mcfriends;

import mcfriends.Utility.TameUtils;
import mcfriends.Utility.PanicAI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class FoxListener implements Listener {
    private boolean foxIsCarried = false;

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.FOX) {
            Fox fox = (Fox) event.getRightClicked();
            Player player = event.getPlayer();

            Material requiredItem = Material.CHICKEN;

            if (TameUtils.tameMob(fox, player, requiredItem)) {
                player.sendMessage("You have tamed the fox!");

                if (TameUtils.tameMobs(fox, player)) {
                    player.sendMessage("The fox will now start following you");
                    fox.setSleeping(false);
                }

            }



            }

        }



    }


