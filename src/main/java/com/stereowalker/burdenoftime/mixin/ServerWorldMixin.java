package com.stereowalker.burdenoftime.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.AgeConversion;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.world.AgeMap;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.profiler.IProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.server.ServerWorld;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Shadow
	public abstract ServerWorld getWorld();

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/chunk/ChunkSection;getBlockState(III)Lnet/minecraft/block/BlockState;", ordinal = 0), method = "tickEnvironment(Lnet/minecraft/world/chunk/Chunk;I)V", locals = LocalCapture.CAPTURE_FAILHARD)
	public void tickEnvironment(Chunk chunkIn, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkpos, boolean flag, int i, int j, IProfiler iprofiler, ChunkSection var8[], int var9, int var10, ChunkSection chunksection, int k, int l, BlockPos blockpos1, BlockState blockstate)
	{
		ageBlock(getWorld(), blockstate, new Random(), blockpos1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ageBlock(ServerWorld world, BlockState blockstate, Random rand, BlockPos pos)
	{
		MinecraftServer server = world.getServer();
		if (server == null)
			return;

		if (Config.chanceForBlockToAge < rand.nextInt(1000))
			return;

		AgeMap ageMapState = AgeMap.getInstance(server, world.getDimensionKey());

		int currentAge = ageMapState.ageMap.getOrDefault(pos, 0) + 1;
		ageMapState.ageMap.put(pos, currentAge);

		ageMapState.setDirty(true);

		for (AgeConversion conversion : Conversions.ageing_conversions)
		{
			if (conversion.from == blockstate.getBlock() && currentAge > conversion.requiredAge)
			{
				BlockState convertedState = conversion.to.getDefaultState();
				for (Property property : blockstate.getProperties()) {
					if (convertedState.hasProperty(property))
						convertedState = convertedState.with(property, blockstate.get(property));
				}
				world.setBlockState(pos, convertedState, 11);
			}
		}
	}
}
