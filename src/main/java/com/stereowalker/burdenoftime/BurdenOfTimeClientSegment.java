package com.stereowalker.burdenoftime;

import java.util.List;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.unionlib.api.collectors.ColorOverrideCollector;
import com.stereowalker.unionlib.client.gui.screens.config.ConfigScreen;
import com.stereowalker.unionlib.mod.ClientSegment;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BurdenOfTimeClientSegment extends ClientSegment {

	@Override
	public Identifier getModIcon() {
		return VersionHelper.toLoc("burdenoftime","textures/icon.png");
	}

	@Override
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new ConfigScreen(previousScreen, Config.class, Component.translatable("burdenoftime.gui.config"));
	}

	@Override
	public void setupColorOverrides(ColorOverrideCollector collector) {
		collector.overrideBlocks(List.of(BlockTintSources.grassBlock()), BurdenOfTime.BlockRegis.transformed);
	}

}
