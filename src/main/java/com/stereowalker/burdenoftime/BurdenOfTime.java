package com.stereowalker.burdenoftime;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.resource.ConversionDataManager;
import com.stereowalker.unionlib.api.collectors.ConfigCollector;
import com.stereowalker.unionlib.api.collectors.ReloadListeners;
import com.stereowalker.unionlib.api.registries.RegistryCollector;
import com.stereowalker.unionlib.core.registries.RegistryHolder;
import com.stereowalker.unionlib.core.registries.RegistryObject;
import com.stereowalker.unionlib.mod.MinecraftMod;
import com.stereowalker.unionlib.mod.ServerSegment;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;

public class BurdenOfTime extends MinecraftMod {
	public static ConversionDataManager data = new ConversionDataManager();
	public static final String ID = "burdenoftime";
	
	
	private static BurdenOfTime instance;
	
	public BurdenOfTime() {
		super(ID, ()-> new BurdenOfTimeClientSegment(), ()-> new ServerSegment());
		instance = this;
	}
	
	@Override
	public void setupConfigs(ConfigCollector collector) {
		collector.registerConfig(Config.class);
	}
	
	@Override
	public void registerServerRelaodableResources(ReloadListeners reloadListener) {
		reloadListener.listenTo(data);
	}
	
	@Override
	public void onModStartup() {
		Conversions.regeisterAllConversions();
	}

	public static BurdenOfTime getInstance() {
		return instance;
	}
	
	@Override
	public void setupRegistries(RegistryCollector collector) {
		collector.addRegistryHolder(Registries.BLOCK, BlockRegis.class);
	}
	
	@RegistryHolder(namespace = ID)
	public static class BlockRegis {
		@RegistryObject("patchy_grass_block_1")
		public static final Block transformed = new GrassBlock(VersionHelper.copyBlockPropertyWithId(Blocks.GRASS_BLOCK, ID+":patchy_grass_block_1"));
	}

}
