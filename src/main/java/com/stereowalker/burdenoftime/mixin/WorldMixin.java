package com.stereowalker.burdenoftime.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.stereowalker.burdenoftime.world.AgeMap;
import com.stereowalker.burdenoftime.world.ErosionMap;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin
{
    @Shadow
    public abstract boolean isRemote();

    @Shadow
    public abstract @Nullable MinecraftServer getServer();

    @Shadow public abstract RegistryKey<World> getDimensionKey();

    @Inject(at = @At("RETURN"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z")
    public void setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue() && !isRemote())
        {
            MinecraftServer server = getServer();
            if (server == null)
                return;

            ErosionMap depthMapState = ErosionMap.getInstance(server, getDimensionKey());
            depthMapState.erosionMap.remove(pos);
            depthMapState.setDirty(true);
            
            AgeMap ageMapState = AgeMap.getInstance(server, getDimensionKey());
            ageMapState.ageMap.remove(pos);
            ageMapState.setDirty(true);
        }
    }
}
