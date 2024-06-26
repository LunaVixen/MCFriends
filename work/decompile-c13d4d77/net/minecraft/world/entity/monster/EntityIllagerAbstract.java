package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.entity.ai.goal.PathfinderGoalDoorOpen;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.raid.EntityRaider;
import net.minecraft.world.level.World;
import org.joml.Vector3f;

public abstract class EntityIllagerAbstract extends EntityRaider {

    protected EntityIllagerAbstract(EntityTypes<? extends EntityIllagerAbstract> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public EnumMonsterType getMobType() {
        return EnumMonsterType.ILLAGER;
    }

    public EntityIllagerAbstract.a getArmPose() {
        return EntityIllagerAbstract.a.CROSSED;
    }

    @Override
    public boolean canAttack(EntityLiving entityliving) {
        return entityliving instanceof EntityVillagerAbstract && entityliving.isBaby() ? false : super.canAttack(entityliving);
    }

    @Override
    protected float ridingOffset(Entity entity) {
        return -0.6F;
    }

    @Override
    protected Vector3f getPassengerAttachmentPoint(Entity entity, EntitySize entitysize, float f) {
        return new Vector3f(0.0F, entitysize.height + 0.05F * f, 0.0F);
    }

    public static enum a {

        CROSSED, ATTACKING, SPELLCASTING, BOW_AND_ARROW, CROSSBOW_HOLD, CROSSBOW_CHARGE, CELEBRATING, NEUTRAL;

        private a() {}
    }

    protected class b extends PathfinderGoalDoorOpen {

        public b(EntityRaider entityraider) {
            super(entityraider, false);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && EntityIllagerAbstract.this.hasActiveRaid();
        }
    }
}
