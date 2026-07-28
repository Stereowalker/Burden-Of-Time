package com.stereowalker.burdenoftime.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.conversions.TrampleErosionConversion;
import com.stereowalker.burdenoftime.world.TrampleErosionMap;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow public Vec3 position() {return null;}
	@Shadow public Vec3 oldPosition() {return null;}
	@Shadow @Final protected RandomSource random;
	@Unique Vec3 oldPosition = new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

	@Shadow public abstract boolean isShiftKeyDown();

	@Shadow protected abstract BlockPos getOnPos();

	@Shadow public Level level;

	@Shadow private boolean onGround;

	@Shadow public abstract Vec3 getDeltaMovement();

	@Shadow
	public abstract boolean isSwimming();

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info)
	{
		if (this.position().subtract(this.oldPosition).length() >= 5d) {
			oldPosition = position();
		}
		if (level.isClientSide())
			return;

		if (isSwimming() || !onGround || !Conversions.trample_conversions.containsKey(RegistryHelper.getBlockKey(level.getBlockState(getOnPos()).getBlock()))) return;
		Vec3 moveVec = this.position().subtract(this.oldPosition);
		double speed = Math.abs(moveVec.length()) * Config.trailSofteningModifier;

		DegradeGround((float) speed);
		
		oldPosition = position();
	}

	private void DegradeGround(float intensity)
	{
		MinecraftServer server = level.getServer();
		if (server == null)
			return;

		if (Config.chanceForGroundToErode < random.nextInt(1000) || intensity < 0.01 || (isShiftKeyDown() && Config.sneakPreventsTrail))
			return;

		TrampleErosionMap depthMapState = TrampleErosionMap.getInstance(server, level.dimension());
		BlockPos pos = getOnPos();
		BlockState state = level.getBlockState(pos);

		float currentDepth = depthMapState.erosionMap.getOrDefault(pos, 0f) + intensity;
		depthMapState.erosionMap.put(pos, currentDepth);


		depthMapState.setDirty(true);
		
		TrampleErosionConversion conversion = Conversions.trample_conversions.get(RegistryHelper.getBlockKey(state.getBlock()));
		conversion.handleConversion(level, pos, state, currentDepth, conversion.requiredDepth);
	}
}
