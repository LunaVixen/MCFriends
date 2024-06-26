package net.minecraft.world.item;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import net.minecraft.world.level.World;

public class ItemArrow extends Item {

    public ItemArrow(Item.Info item_info) {
        super(item_info);
    }

    public EntityArrow createArrow(World world, ItemStack itemstack, EntityLiving entityliving) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, entityliving, itemstack.copyWithCount(1));

        entitytippedarrow.setEffectsFromItem(itemstack);
        return entitytippedarrow;
    }
}
