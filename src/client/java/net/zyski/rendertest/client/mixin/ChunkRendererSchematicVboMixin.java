package net.zyski.rendertest.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fi.dy.masa.litematica.render.schematic.BufferAllocatorCache;
import fi.dy.masa.litematica.render.schematic.ChunkCacheSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDataSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zyski.rendertest.client.RendertestClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ChunkRendererSchematicVbo.class)
public class ChunkRendererSchematicVboMixin {


    @Shadow
    protected ChunkCacheSchematic schematicWorldView;

    @Inject(method = "renderBlocksAndOverlay", at = @At("HEAD"), cancellable = true)
    public void renderBlocksAndOverlay(BlockPos pos, ChunkRenderDataSchematic data, BufferAllocatorCache allocators, Set<RenderType> usedLayers, PoseStack matrixStack, CallbackInfo ci) {
        if (RendertestClient.specific) {
            ItemStack held = Minecraft.getInstance().player.getMainHandItem();
            Item heldItem = held.getItem();
            BlockState stateSchematic = schematicWorldView.getBlockState(pos);
            Item schematicItem = stateSchematic.getBlock().asItem();

            if (!held.isEmpty() && schematicItem != heldItem) {
                ci.cancel();
            }
        }
    }

}
