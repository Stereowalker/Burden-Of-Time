package com.stereowalker.burdenoftime;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.unionlib.client.gui.screens.config.ConfigScreen;
import com.stereowalker.unionlib.mod.ClientSegment;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BurdenOfTimeClientSegment extends ClientSegment {

	@Override
	public ResourceLocation getModIcon() {
		return VersionHelper.toLoc("burdenoftime","textures/icon.png");
	}
	
	@Override
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new ConfigScreen(previousScreen, Config.class, Component.translatable("burdenoftime.gui.config"));
	}

}
