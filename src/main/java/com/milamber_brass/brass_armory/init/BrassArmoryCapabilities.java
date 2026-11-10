package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.capabilities.*;
import java.util.function.Supplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BrassArmoryCapabilities {
    public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, BrassArmory.MOD_ID);
    public static final Supplier<AttachmentType<QuiverCapabilityHandler>> QUIVER_CAPABILITY = REGISTRY.register("quiver_capability", () -> AttachmentType.serializable(holder -> {
        QuiverCapabilityHandler handler = new QuiverCapabilityHandler();
        handler.setEntity((Player) holder);
        return handler;
    }).build());
    public static final Supplier<AttachmentType<EffectCapabilityHandler>> EFFECT_CAPABILITY = REGISTRY.register("effect_capability", () -> AttachmentType.serializable(holder -> {
        EffectCapabilityHandler handler = new EffectCapabilityHandler();
        handler.setEntity((Player) holder);
        return handler;
    }).build());
    public static final Supplier<AttachmentType<PowderCapabilityHandler>> POWDER_CAPABILITY = REGISTRY.register("powder_capability", () -> AttachmentType.serializable(holder -> {
        PowderCapabilityHandler handler = new PowderCapabilityHandler();
        handler.setEntity((Projectile) holder);
        return handler;
    }).build());
}
