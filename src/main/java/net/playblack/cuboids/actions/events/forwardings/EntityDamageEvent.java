package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.gameinterface.IBaseEntity;

public class EntityDamageEvent extends CuboidEvent implements Cancellable {

    public enum DamageSource {

        CREEPER_EXPLOSION,
        ENTITY,
        EXPLOSION,
        FALL,
        FIRE,
        FIRE_TICK,
        LAVA,
        WATER,
        CACTUS,
        SUFFOCATION,
        LIGHTING,
        STARVATION,
        POTION,
        WITHER_SKULL,
        ENDERPEARL,
        FALLING_ANVIL, //So funny :D
        FALLING_BLOCK,
        GENERIC;
    }
    private boolean isCancelled = false;
    private IBaseEntity attacker;
    private IBaseEntity defender;
    private DamageSource damageSource;
    private float damage;

    public EntityDamageEvent(IBaseEntity attacker, IBaseEntity defender, DamageSource damageSource, float damage) {
        this.attacker = attacker;
        this.defender = defender;
        this.damageSource = damageSource;
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public IBaseEntity getAttacker() {
        return attacker;
    }

    public IBaseEntity getDefender() {
        return defender;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public float getDamage() {
        return damage;
    }

    /**
     * Override the damage done
     *
     * @param newDamage
     */
    public void setDamage(int newDamage) {
        this.damage = newDamage;
    }
}
