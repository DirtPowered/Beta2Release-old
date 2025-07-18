package com.github.dirtpowered.betatorelease.data.utils;

import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;

import java.util.Objects;

public class OldMobType {

    public static byte fromMobType(MobType value) {
        byte id;
        if (Objects.requireNonNull(value) == MobType.CREEPER) {
            id = 50;
        } else if (value == MobType.PIG) {
            id = 90;
        } else if (value == MobType.COW || value == MobType.MOOSHROOM || value == MobType.HORSE || value == MobType.MULE || value == MobType.DONKEY || value == MobType.SKELETON_HORSE || value == MobType.ZOMBIE_HORSE) {
            id = 92;
        } else if (value == MobType.WOLF || value == MobType.OCELOT) {
            id = 95;
        } else if (value == MobType.GHAST || value == MobType.ENDER_DRAGON || value == MobType.WITHER) {
            id = 56;
        } else if (value == MobType.SHEEP || value == MobType.POLAR_BEAR) {
            id = 91;
        } else if (value == MobType.SLIME || value == MobType.MAGMA_CUBE) {
            id = 55;
        } else if (value == MobType.SPIDER || value == MobType.CAVE_SPIDER || value == MobType.SILVERFISH || value == MobType.ENDERMITE) {
            id = 52;
        } else if (value == MobType.ZOMBIE || value == MobType.HUSK || value == MobType.ZOMBIE_VILLAGER) {
            id = 54;
        } else if (value == MobType.CHICKEN || value == MobType.RABBIT || value == MobType.BAT || value == MobType.PARROT) {
            id = 93;
        } else if (value == MobType.SQUID || value == MobType.GUARDIAN || value == MobType.ELDER_GUARDIAN) {
            id = 94;
        } else if (value == MobType.SKELETON || value == MobType.WITHER_SKELETON || value == MobType.SNOWMAN || value == MobType.WITCH || value == MobType.SHULKER || value == MobType.BLAZE) {
            id = 51;
        } else if (value == MobType.ZOMBIE_PIGMAN || value == MobType.VILLAGER || value == MobType.IRON_GOLEM || value == MobType.VINDICATION_ILLAGER || value == MobType.EVOCATION_ILLAGER || value == MobType.ILLUSION_ILLAGER) {
            id = 57;
        } else if (value == MobType.ENDERMAN) {
            id = 54;
        } else if (value == MobType.GIANT_ZOMBIE) {
            id = 53;
        } else {
            id = 90; // All unknown entities should be visible anyway. Enderdragon too. Flying pig *_*
        }
        return id;
    }
}
