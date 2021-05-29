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

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.profiler.IProfiler;
import net.minecraft.server.MinecraftServer;
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
		erodeBlock(getWorld(), blockstate, new Random(), blockpos1);
	}

	private void ageBlock(ServerWorld world, BlockState blockstate, Random rand, BlockPos pos)
	{
		MinecraftServer server = world.getServer();
		if (server == null)
			return;

		if (Config.chanceForBlockToAge < rand.nextInt(1000))
			return;

		AgeErosionMap ageMapState = AgeErosionMap.getInstance(server, world.getDimensionKey());

		int currentAge = ageMapState.ageMap.getOrDefault(pos, 0) + 1;
		ageMapState.ageMap.put(pos, currentAge);

		ageMapState.setDirty(true);

		for (AgeErosionConversion conversion : Conversions.ageing_conversions)
		{
			conversion.handleConversion(world, pos, blockstate, currentAge, conversion.requiredAge);
		}
	}

	private void erodeBlock(ServerWorld world, BlockState blockstate, Random rand, BlockPos pos)
	{
		MinecraftServer server = world.getServer();
		if (server == null)
			return;
		
		if (world.getFluidState(pos.up()).isEmpty()
				&& world.getFluidState(pos.north()).isEmpty()
				&& world.getFluidState(pos.south()).isEmpty()
				&& world.getFluidState(pos.west()).isEmpty()
				&& world.getFluidState(pos.east()).isEmpty())
			return;

		if (Config.chanceForBlockToErode < rand.nextInt(1000))
			return;


		for (FluidErosionConversion conversion : Conversions.erosion_conversions)
		{
			if (world.getFluidState(pos.up()).getFluid() == conversion.requiredFluid 
					|| world.getFluidState(pos.north()).getFluid() == conversion.requiredFluid
					|| world.getFluidState(pos.south()).getFluid() == conversion.requiredFluid
					|| world.getFluidState(pos.west()).getFluid() == conversion.requiredFluid
					|| world.getFluidState(pos.east()).getFluid() == conversion.requiredFluid) {

				FluidErosionMap ageMapState = FluidErosionMap.getInstance(server, world.getDimensionKey());

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
