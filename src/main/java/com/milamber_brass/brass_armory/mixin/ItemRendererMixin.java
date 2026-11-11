package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.QuiverItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public abstract class ItemRendererMixin {
    @Inject(method = "appendItemLayers", at = @At("TAIL"))
    private void appendArmoryContents(ItemStackRenderState output, ItemStack stack, ItemDisplayContext context, Level level, ItemOwner owner, int seed, CallbackInfo ci) {
        if (output.isEmpty()) return;
        ItemStack contents;
        Matrix4f transform = new Matrix4f();
        if (stack.is(BrassArmoryItems.TORCH_ARROW.get())) {
            contents = ArmoryUtil.loadStack(ItemData.getTag(stack), "BATorch", Items.TORCH.getDefaultInstance());
            transform.translate(0.25F, 0.25F, 0F);
        } else if (stack.getItem() instanceof QuiverItem) {
            contents = QuiverItem.getContents(stack).findFirst().orElse(ItemStack.EMPTY);
            transform.translate(-0.5F, -0.5F, -0.5F).rotateY((float) Math.PI).translate(-0.9375F, 0.0625F, -0.875F).scale(1F, 1F, 0.75F).translate(0.5F, 0.5F, 0.5F);
        } else return;
        if (contents.isEmpty() || contents.getItem() instanceof QuiverItem || contents.is(stack.getItem())) return;
        var parent = output.layers[0];
        var parentTransform = parent.itemTransform;
        transform = new Matrix4f(parent.localTransform).mul(transform);
        int first = output.activeLayerCount;
        ((ItemModelResolver) (Object) this).appendItemLayers(output, contents, ItemDisplayContext.NONE, level, owner, seed);
        for (int i = first; i < output.activeLayerCount; i++) {
            var layer = output.layers[i];
            layer.setItemTransform(parentTransform);
            layer.setLocalTransform(new Matrix4f(transform).mul(layer.localTransform));
        }
    }
}
