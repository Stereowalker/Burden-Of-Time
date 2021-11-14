package com.stereowalker.burdenoftime.mixin;

import java.util.HashMap;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.AgeErosionConversion;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.conversions.FluidErosionConversion;
import com.stereowalker.burdenoftime.world.AgeErosionMap;
import com.stereowalker.burdenoftime.world.FluidErosionMap;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.Fluid;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin
{
	@Shadow
	public abstract ServerLevel getLevel();

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0), method = "tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V", locals = LocalCapture.CAPTURE_FAILHARD)
	public void tickEnvironment(LevelChunk chunkIn, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkpos, boolean flag, int i, int j, ProfilerFiller iprofiler, LevelChunkSection var8[], int var9, int var10, LevelChunkSection chunksection, int k, int l, BlockPos blockpos1, BlockState blockstate)
	{
		if (Conversions.tickable_blocks.contains(blockstate.getBlock())) {			
			ageBlock(getLevel(), blockstate, new Random(), blockpos1);
			erodeBlock(getLevel(), blockstate, new Random(), blockpos1);
		}
	}

	private void ageBlock(ServerLevel world, BlockState blockstate, Random rand, BlockPos pos)
	{
		MinecraftServer server = world.getServer();
		if (server == null)
			return;

		if (Config.chanceForBlockToAge < rand.nextInt(1000))
			return;

		AgeErosionMap ageMapState = AgeErosionMap.getInstance(server, world.dimension());

		int currentAge = ageMapState.ageMap.getOrDefault(pos, 0) + 1;
		ageMapState.ageMap.put(pos, currentAge);

		ageMapState.setDirty(true);

		for (AgeErosionConversion conversion : Conversions.ageing_conversions)
		{
			conversion.handleConversion(world, pos, blockstate, currentAge, conversion.requiredAge);
		}
	}

	private void erodeBlock(ServerLevel world, BlockState blockstate, Random rand, BlockPos pos)
	{
		MinecraftServer server = world.getServer();
		if (server == null)
			return;
		
		if (world.getFluidState(pos.above()).isEmpty()
				&& world.getFluidState(pos.north()).isEmpty()
				&& world.getFluidState(pos.south()).isEmpty()
				&& world.getFluidState(pos.west()).isEmpty()
				&& world.getFluidState(pos.east()).isEmpty())
			return;

		if (Config.chanceForBlockToErode < rand.nextInt(1000))
			return;


		for (FluidErosionConversion conversion : Conversions.fluid_conversions)
		{
			if (world.getFluidState(pos.above()).getType() == conversion.requiredFluid 
					|| world.getFluidState(pos.north()).getType() == conversion.requiredFluid
					|| world.getFluidState(pos.south()).getType() == conversion.requiredFluid
					|| world.getFluidState(pos.west()).getType() == conversion.requiredFluid
					|| world.getFluidState(pos.east()).getType() == conversion.requiredFluid) {

				FluidErosionMap ageMapState = FluidErosionMap.getInstance(server, world.dimension());

				HashMap<Fluid, Integer> wearAge = ageMapState.wearMap.getOrDefault(pos, new HashMap<>());
				int currentAge = wearAge.getOrDefault(conversion.requiredFluid, 0) + 10;
				wearAge.put(conversion.requiredFluid, currentAge);
				ageMapState.wearMap.put(pos, wearAge);

				ageMapState.setDirty(true);
				conversion.handleConversion(world, pos, blockstate, currentAge, conversion.requiredAge);
			}
		}
	}
}
