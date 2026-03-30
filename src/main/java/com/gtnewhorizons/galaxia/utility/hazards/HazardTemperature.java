package com.gtnewhorizons.galaxia.utility.hazards;

import static com.gtnewhorizons.galaxia.utility.GalaxiaAPI.getThermalProtection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import com.gtnewhorizons.galaxia.registry.dimension.builder.EffectBuilder;
import com.gtnewhorizons.galaxia.utility.effects.GalaxiaEffects;

public class HazardTemperature extends EnvironmentalHazard {

    private static DamageSource temperatureDamage = new DamageSource("galaxia.temperature").setDamageBypassesArmor()
        .setMagicDamage();

    public static int DEFAULT_MIN = 268; // -5 Celsius
    public static int DEFAULT_MAX = 323; // 50 Celsius

    // Temp until space suit added
    public int acceptableMinTemp = DEFAULT_MIN;
    public int acceptableMaxTemp = DEFAULT_MAX;

    /**
     * Applies the effects of Temperature for the player:
     * Diff < 20K = Slowness/Fatigue 1
     * 20K <= Diff <= 50K = Slowness/Fatigue 2
     * Diff > 50K = Slowness/Fatigue 3 and 1 heart per second damange
     *
     * @param def    The EffectDef of the dimension
     * @param player The player entity
     * @return
     */
    @Override
    public HazardWarnings apply(EffectBuilder def, EntityPlayer player) {
        int temp = def.getTemperature(player);
        int heatProtection = getThermalProtection(player, true);
        int coldProtection = getThermalProtection(player, false);

        this.acceptableMaxTemp += heatProtection;
        this.acceptableMinTemp -= coldProtection;

        if (temp < this.acceptableMaxTemp && temp > this.acceptableMinTemp) return null;
        if (temp < this.acceptableMinTemp) {
            return applyFreeze(player, temp);
        } else if (temp > this.acceptableMaxTemp) {
            return applyBurning(player, temp);
        }

        return HazardWarnings.FINE;
    }

    private HazardWarnings applyFreeze(EntityPlayer player, int temperature) {
        final int diff = this.acceptableMinTemp - temperature;
        final int harshness;
        if (diff < 20) {
            harshness = 0;
        } else if (diff < 50) {
            harshness = 1;
        } else {
            harshness = 2;
        }

        player.addPotionEffect(new PotionEffect(GalaxiaEffects.freezing.id, BASE_EFFECT_DURATION, harshness));
        if (harshness == 2) {
            player.attackEntityFrom(temperatureDamage, 2.0f);
        }

        return HazardWarnings.FREEZING;
    }

    private HazardWarnings applyBurning(EntityPlayer player, int temperature) {
        final int diff = temperature - this.acceptableMaxTemp;
        final int harshness;
        if (diff < 20) {
            harshness = 0;
        } else if (diff < 50) {
            harshness = 1;
        } else {
            harshness = 2;
        }

        player.addPotionEffect(new PotionEffect(GalaxiaEffects.overheating.id, BASE_EFFECT_DURATION, harshness));
        return HazardWarnings.BURNING;
    }
}
