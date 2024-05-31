package mcfriends.Utility;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public class PanicAI {
    private Mob mob;
    private boolean isTamed;
    private boolean isCarriedByPlayer;

    public PanicAI(Mob mob) {
        this.mob = mob;
        this.isTamed = false; // Initialize as not tamed
    }

    public void onTick() {
        if (isTamed) { // Check if the mob is tamed
            return; // Do nothing, no panic
        } else {
            // Original panic AI logic goes here
        }
    }

    public void setCarriedByPlayer(Player player) {
        this.isCarriedByPlayer = true;
    }

    public void resetCarriedByPlayer() {
        this.isCarriedByPlayer = false;
    }

    public void setTamed(boolean tamed) {
        this.isTamed = tamed;
    }
}
