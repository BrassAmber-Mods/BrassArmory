package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WarpArrowEntity extends AbstractSpecialArrowEntity {
    public WarpArrowEntity(EntityType<? extends WarpArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public WarpArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.WARP_ARROW.get(), level, shooter);
    }

    public WarpArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.WARP_ARROW.get(), level, x, y, z);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.dealtDamage) this.teleportShooter(result.getLocation());
        super.onHitBlock(result);
    }

    private void teleportShooter(Vec3 pos) {
        Entity entity = this.getOwner();
        if (!this.level.isClientSide && this.isAlive()) {
            // Check if the shooter is a Player.
            if (entity instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.connection.getConnection().isConnected() && serverPlayer.level == this.level && !serverPlayer.isSleeping()) {

                    net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl event = ForgeEventFactory.onEnderPearlLand(serverPlayer, pos.x, pos.y, pos.z, EntityType.ENDER_PEARL.create(this.level), 5.0F);
                    if (!event.isCanceled()) {
                        // Small chance to spawn an Endermite.
                        if (this.random.nextFloat() < 0.05F && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                            Endermite endermite = EntityType.ENDERMITE.create(this.level);

                            if (endermite != null) {
                                endermite.moveTo(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                                this.level.addFreshEntity(endermite);
                            }
                        }

                        if (serverPlayer.distanceTo(this) >= 100D) BrassArmoryAdvancements.LONG_DISTANCE_WARP_ARROW_TRIGGER.trigger(serverPlayer);

                        if (serverPlayer.isPassenger()) {
                            serverPlayer.dismountTo(pos.x, pos.y, pos.z);
                        } else {
                            entity.teleportTo(pos.x, pos.y, pos.z);
                        }

                        entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        entity.resetFallDistance();
                    }
                }
            } else if (entity != null) {
                entity.teleportTo(pos.x, pos.y, pos.z);
                entity.resetFallDistance();
            }

            this.discard();
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.WARP_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        for (int j = 0; j < particleCount; ++j) {
            this.level.addParticle(ParticleTypes.REVERSE_PORTAL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0D, 0D, 0D);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.WARP_ARROW_TEXTURE;
    }
}