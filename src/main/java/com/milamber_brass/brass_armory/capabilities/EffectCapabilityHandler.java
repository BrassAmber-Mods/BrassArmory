package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.init.BrassArmoryPackets;
import com.milamber_brass.brass_armory.packets.EffectPacket;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EffectCapabilityHandler implements IEffectCapability {
    private Player player;
    private double shake;
    private float slow;

    @Override
    public void setEntity(Player player) {
        this.player = player;
    }

    @Override
    public void setShake(double shake) {
        if (player instanceof ServerPlayer) {
            this.shake = shake;
            this.sendUpdatePacket();
        } else this.shake = Math.max(this.shake, shake);
    }

    @Override
    public void reduceShake(double d) {
        this.shake -= d;
        if (this.shake < 0.01D) this.shake = 0.0D;
    }

    @Override
    public double getShake() {
        return this.shake;
    }

    @Override
    public void setSlow(float slow) {
        this.slow = slow;
        if (!this.player.level.isClientSide) this.sendUpdatePacket();
    }

    @Override
    public float getSlow() {
        return this.slow;
    }

    @Override
    public void tick() {
        if (!this.player.level.isClientSide) {
            if (this.shake > 0.0F) this.reduceShake(this.shake * 0.1D);
            if (this.slow > 0.0F) this.setSlow(Math.max(this.slow - 0.005F, 0.0F));
        }
    }

    private void sendUpdatePacket() {
        BrassArmoryPackets.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.player), new EffectPacket(this.player, this));
    }

    @Override
    public CompoundTag serializeNBT() {
        return Util.make(new CompoundTag(), tag -> {
            tag.putDouble("BAShakeValue", this.getShake());
            tag.putFloat("BASlowValue", this.getSlow());
        });
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.setShake(tag.getDouble("BAShakeValue"));
        this.setSlow(tag.getFloat("BASlowValue"));
    }

    public static void setShakePower(Player player, double shake) {
        player.getCapability(BrassArmoryCapabilities.EFFECT_CAPABILITY).ifPresent(iShakeCapability -> iShakeCapability.setShake(shake));
    }
}
