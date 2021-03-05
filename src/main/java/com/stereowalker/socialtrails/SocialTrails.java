package com.stereowalker.socialtrails;

import com.stereowalker.socialtrails.config.Config;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.mod.UnionMod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = "socialtrails")
public class SocialTrails extends UnionMod {

	public static SocialTrails instance;
	
	public SocialTrails() {
		super("footpaths", new ResourceLocation("socialtrails","textures/icon.png"), LoadType.BOTH);
		instance = this;
		ConfigBuilder.registerConfig(Config.class);
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
	}

	public void clientRegistries(final FMLClientSetupEvent event)
	{
	}

}
