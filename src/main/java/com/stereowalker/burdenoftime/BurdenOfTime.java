package com.stereowalker.burdenoftime;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.resource.ConversionDataManager;
import com.stereowalker.unionlib.client.gui.screens.config.ConfigScreen;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.mod.MinecraftMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = "burdenoftime")
public class BurdenOfTime extends MinecraftMod {
	public static ConversionDataManager data = new ConversionDataManager();
	
	
	private static BurdenOfTime instance;
	
	public BurdenOfTime() {
		super("burdenoftime", new ResourceLocation("burdenoftime","textures/icon.png"), LoadType.BOTH);
		instance = this;
		ConfigBuilder.registerConfig(Config.class);
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		//MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		Conversions.regeisterAllConversions();
	}

	public void clientRegistries(final FMLClientSetupEvent event)
	{
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public Screen getConfigScreen(Minecraft mc, Screen previousScreen) {
		return new ConfigScreen(previousScreen, Config.class, new TranslatableComponent("burdenoftime.gui.config"));
	}

	public static BurdenOfTime getInstance() {
		return instance;
	}
	
	@EventBusSubscriber
	public static class E {
		@SubscribeEvent
		public static void eve(AddReloadListenerEvent event) {
			event.addListener(data);
		}
	}

}
