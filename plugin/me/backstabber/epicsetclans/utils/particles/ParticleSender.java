package me.backstabber.epicsetclans.utils.particles;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

/**
 * Particle sender using the Bukkit particles API for 1.9+ servers
 *
 * @author MrMicky
 */
interface ParticleSender {

    void spawnParticle(Object receiver, ParticleType particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data);

    Object getParticle(ParticleType particle);

    boolean isValidData(Object particle, Object data);

    default double color(double color) {
        return color / 255.0;
    }

    class ParticleSenderImpl implements ParticleSender 
    {

        @Override
        public void spawnParticle(Object receiver, ParticleType particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
            Particle bukkitParticle = Particle.valueOf(particle.toString());

            if (data instanceof Color) 
            {
                if (particle.getDataType() == Color.class) 
                {
                    Color color = (Color) data;
                    count = 0;
                    offsetX = color(color.getRed());
                    offsetY = color(color.getGreen());
                    offsetZ = color(color.getBlue());
                    extra = 1.0;
                	data = getData(color, 1);
                }
                else
                	data = getData(Color.RED, 1);
            }

            if (receiver instanceof World) 
            {
                ((World) receiver).spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
            } else if (receiver instanceof Player) 
            {
                ((Player) receiver).spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
            }
        }

		@Override
        public Particle getParticle(ParticleType particle) {
            try {
                return Particle.valueOf(particle.toString());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        @Override
        public boolean isValidData(Object particle, Object data) {
            return isValidDataBukkit((Particle) particle, data);
        }

        public boolean isValidDataBukkit(Particle particle, Object data) {
            return particle.getDataType() == Void.class || particle.getDataType().isInstance(data);
        }
        private Object getData(Color color,int amount)
        {
        	if(color==null) {
                color = Color.RED;
            }

        	if(amount<=0) {
                amount = 1;
            }
            
        	return new DustOptions(color, amount);
        }
    }
}