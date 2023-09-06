package com.stereowalker.burdenoftime;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.resource.ConversionDataManager;
import com.stereowalker.unionlib.api.collectors.ConfigCollector;
import com.stereowalker.unionlib.api.collectors.ReloadListeners;
import com.stereowalker.unionlib.mod.MinecraftMod;
import com.stereowalker.unionlib.mod.ServerSegment;

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

}
