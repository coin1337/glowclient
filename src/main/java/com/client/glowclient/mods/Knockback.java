//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.client.glowclient.mods;

import com.client.glowclient.sponge.events.PacketEvent;
import com.client.glowclient.utils.base.mod.Category;
import com.client.glowclient.utils.base.mod.branches.ToggleMod;
import com.client.glowclient.utils.base.setting.SettingUtils;
import com.client.glowclient.utils.base.setting.branches.SettingDouble;
import com.client.glowclient.utils.client.Globals;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Knockback
extends ToggleMod {
    public static final SettingDouble multiplier_h = SettingUtils.settingDouble("Knockback", "Horizontal", "Multiplier for horizontal movement", 0.0, 0.1, -5.0, 5.0);
    public static final SettingDouble multiplier_y = SettingUtils.settingDouble("Knockback", "Vertical", "Multiplier for Vertical movement", 0.0, 0.1, -5.0, 5.0);

    public Knockback() {
        super(Category.COMBAT, "Knockback", false, -1, "Changes knockback movement");
    }

    @Override
    public String getHUDTag() {
        String mode = String.format("H:%.0f,", multiplier_h.getDouble());
        String mode2 = String.format("V:%.0f", multiplier_y.getDouble());
        return mode + mode2;
    }

    @SubscribeEvent
    public void onPacketRecieved(PacketEvent.Receive event) {
        try {
            SPacketExplosion packet;
            double multiX = multiplier_h.getDouble();
            double multiY = multiplier_y.getDouble();
            double multiZ = multiplier_h.getDouble();
            if (event.getPacket() instanceof SPacketExplosion) {
                if (multiX == 0.0 && multiY == 0.0 && multiZ == 0.0) {
                    event.setCanceled(true);
                } else {
                    packet = (SPacketExplosion)event.getPacket();
                    packet.motionX = (float)((double)packet.motionX * multiX);
                    packet.motionY = (float)((double)packet.motionY * multiY);
                    packet.motionZ = (float)((double)packet.motionZ * multiZ);
                }
            }
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Globals.MC.player.getEntityId()) {
                if (multiX == 0.0 && multiY == 0.0 && multiZ == 0.0) {
                    event.setCanceled(true);
                } else {
                    packet = (SPacketEntityVelocity)event.getPacket();
                    packet.motionX = (int)((double)packet.motionX * multiX);
                    packet.motionY = (int)((double)packet.motionY * multiY);
                    packet.motionZ = (int)((double)packet.motionZ * multiZ);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

