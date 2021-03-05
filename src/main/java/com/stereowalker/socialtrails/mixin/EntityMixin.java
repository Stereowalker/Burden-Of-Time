package com.stereowalker.socialtrails.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.socialtrails.blocks.TrailConversions;
import com.stereowalker.socialtrails.config.Config;
import com.stereowalker.socialtrails.world.ErosionMap;

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

		ErosionMap depthMapState = ErosionMap.getInstance(server, world.getDimensionKey());
		BlockPos pos = getOnPosition();
		BlockState state = world.getBlockState(pos);

		float currentDepth = depthMapState.erosionMap.getOrDefault(pos, 0f) + intensity;
		depthMapState.erosionMap.put(pos, currentDepth);

		currentDepth *= Config.trailSofteningModifier;

		depthMapState.setDirty(true);

		for (TrailConversions conversion : Config.conversions)
		{
			if (conversion.from == state.getBlock() && currentDepth > conversion.requiredDepth)
			{
				world.setBlockState(pos, conversion.to.getDefaultState(), 11);
			}
		}
	}
}
