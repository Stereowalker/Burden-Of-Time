package com.stereowalker.burdenoftime.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.conversions.TrampleErosionConversion;
import com.stereowalker.burdenoftime.world.TrampleErosionMap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow public float prevDistanceWalkedModified;
	@Shadow public float distanceWalkedModified;
	@Shadow @Final protected Random rand;

	@Shadow public abstract boolean isSneaking();

	@Shadow protected abstract BlockPos getOnPosition();

	@Shadow public World world;

	@Shadow public abstract boolean isOnGround();

	@Shadow public abstract Vector3d getMotion();

	@Shadow
	public abstract boolean isSwimming();

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info)
	{
		if (world.isRemote())
			return;

		if (isSwimming() || !isOnGround()) return;

		double speed = Math.abs(prevDistanceWalkedModified - distanceWalkedModified);

		DegradeGround((float) speed);
	}

	private void DegradeGround(float intensity)
	{
		MinecraftServer server = world.getServer();
		if (server == null)
			return;

		if (Config.chanceForGroundToErode < rand.nextInt(1000) || intensity < 0.01 || (isSneaking() && Config.sneakPreventsTrail))
			return;

		TrampleErosionMap depthMapState = TrampleErosionMap.getInstance(server, world.getDimensionKey());
		BlockPos pos = getOnPosition();
		BlockState state = world.getBlockState(pos);

		float currentDepth = depthMapState.erosionMap.getOrDefault(pos, 0f) + intensity;
		depthMapState.erosionMap.put(pos, currentDepth);

		currentDepth *= Config.trailSofteningModifier;

		depthMapState.setDirty(true);

		for (TrampleErosionConversion conversion : Conversions.trample_conversions)
		{
			conversion.handleConversion(world, pos, state, currentDepth, conversion.requiredDepth);
		}
	}
}
