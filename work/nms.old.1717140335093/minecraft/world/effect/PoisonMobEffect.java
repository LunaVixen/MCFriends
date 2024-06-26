package net.minecraft.world.effect;

import net.minecraft.world.entity.EntityLiving;

class PoisonMobEffect extends MobEffectList {

    protected PoisonMobEffect(MobEffectInfo mobeffectinfo, int i) {
        super(mobeffectinfo, i);
    }

    @Override
    public void applyEffectTick(EntityLiving entityliving, int i) {
        super.applyEffectTick(entityliving, i);
        if (entityliving.getHealth() > 1.0F) {
            entityliving.hurt(entityliving.damageSources().poison(), 1.0F);  // CraftBukkit - DamageSource.MAGIC -> CraftEventFactory.POISON
        }

    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        int k = 25 >> j;

        return k > 0 ? i % k == 0 : true;
    }
}
