package com.stereowalker.burdenoftime;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.unionlib.client.gui.screen.ConfigScreen;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.mod.UnionMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = "burdenoftime")
public class BurdenOfTime extends UnionMod {

	public static BurdenOfTime instance;
	
	public BurdenOfTime() {
		super("burdenoftime", new ResourceLocation("burdenoftime","textures/icon.png"), LoadType.BOTH);
		instance = this;
		ConfigBuilder.registerConfig(Config.class);
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		Conversions.regeisterAllConversions();
	}

	public void clientRegistries(final FMLClientSetupEvent event)
	{
	}
	
	@Override
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new ConfigScreen(previousScreen, Config.class, new TranslationTextComponent("burdenoftime.gui.config"));
	}

}
