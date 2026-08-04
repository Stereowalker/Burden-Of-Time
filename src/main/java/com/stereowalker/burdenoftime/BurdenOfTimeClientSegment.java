package com.stereowalker.burdenoftime;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.unionlib.api.collectors.ColorOverrideCollector;
import com.stereowalker.unionlib.api.collectors.RenderLayerCollector;
import com.stereowalker.unionlib.client.gui.screens.config.ConfigScreen;
import com.stereowalker.unionlib.mod.ClientSegment;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;

public class BurdenOfTimeClientSegment extends ClientSegment {

	@Override
	public ResourceLocation getModIcon() {
		return VersionHelper.toLoc("burdenoftime","textures/icon.png");
	}

	@Override
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new ConfigScreen(previousScreen, Config.class, Component.translatable("burdenoftime.gui.config"));
	}

	@Override
	public void setupRenderLayers(RenderLayerCollector collector) {
		collector.setBlockRenderLayer(RenderType.cutoutMipped(), BurdenOfTime.BlockRegis.transformed);
	}

	@Override
	public void setupColorOverrides(ColorOverrideCollector collector) {
		collector.overrideBlocks((state, tintGetter, pos, index) -> {
			return tintGetter != null && pos != null ? BiomeColors.getAverageGrassColor(tintGetter, pos) : GrassColor.getDefaultColor();
		}, BurdenOfTime.BlockRegis.transformed);
	}

}
