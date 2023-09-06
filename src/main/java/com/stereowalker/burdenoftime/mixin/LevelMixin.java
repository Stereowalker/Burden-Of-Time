package com.stereowalker.burdenoftime.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.burdenoftime.world.AgeErosionMap;
import com.stereowalker.burdenoftime.world.FluidErosionMap;
import com.stereowalker.burdenoftime.world.TrampleErosionMap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Level.class)
public abstract class LevelMixin
{
    @Shadow
    public abstract boolean isClientSide();

    @Shadow
    public abstract @Nullable MinecraftServer getServer();

    @Shadow public abstract ResourceKey<Level> dimension();

    @Inject(at = @At("RETURN"), method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z")
    public void setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue() && !isClientSide())
        {
            MinecraftServer server = getServer();
            if (server == null)
                return;

            TrampleErosionMap depthMapState = TrampleErosionMap.getInstance(server, dimension());
            depthMapState.erosionMap.remove(pos);
            depthMapState.setDirty(true);
            
            AgeErosionMap ageMapState = AgeErosionMap.getInstance(server, dimension());
            ageMapState.ageMap.remove(pos);
            ageMapState.setDirty(true);
            
            FluidErosionMap fluidMapState = FluidErosionMap.getInstance(server, dimension());
            fluidMapState.wearMap.remove(pos);
            fluidMapState.setDirty(true);
        }
    }
}
