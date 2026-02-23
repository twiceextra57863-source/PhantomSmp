package com.yourname.smpstarter.manager;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleManager {

    public void spawnFireParticles(Location loc) {
        if (loc.getWorld() != null) {
            loc.getWorld().spawnParticle(Particle.FLAME, loc, 30, 0.3, 0.3, 0.3, 0.05);
            loc.getWorld().spawnParticle(Particle.LAVA, loc, 10, 0.3, 0.3, 0.3, 0.1);
        }
    }

    public void spawnHealParticles(Location loc) {
        if (loc.getWorld() != null) {
            loc.getWorld().spawnParticle(Particle.HEART, loc, 15, 0.5, 1.0, 0.5, 0.5);
            loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 20, 0.5, 1.0, 0.5, 0.1);
        }
    }

    public void spawnSpeedParticles(Location loc) {
        if (loc.getWorld() != null) {
            loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.5, 0.5, 0.5, 0.1);
            loc.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1);
        }
    }
}