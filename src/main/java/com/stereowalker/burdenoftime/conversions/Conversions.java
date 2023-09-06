package com.stereowalker.burdenoftime.conversions;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stereowalker.burdenoftime.BurdenOfTime;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Conversions {
	public static Map<ResourceLocation, TrampleErosionConversion> trample_conversions = Maps.newHashMap();
	
	public static void registerTrampleConversions(String from, String to, float requiredDepth) {
		if (ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from)) && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
			trample_conversions.put(new ResourceLocation(from), new TrampleErosionConversion(from, to, requiredDepth));
		} else {
			String message = "";
			boolean flag = false;
			if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from))) message = from;
			if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
				if (message.isEmpty()) message = to;
				else {
					message = from + " and " + to;
					flag = true;
				}
			}
			BurdenOfTime.getInstance().getLogger().warn("The Block"+(flag?"s ":" ")+message+" does not currently exist. If "+(flag?"these blocks are ":"this block is ")+" from another mod, "
					+ "then install that mod for this trample conversion to work. If the required mod is already installed and you get this message, then report this issue to the mod developer");
		}
	}

	public static Map<ResourceLocation, AgeErosionConversion> ageing_conversions = Maps.newHashMap();

	public static void registerAgeConversions(String from, String to, int requiredAge) {
		if (ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from)) && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
			ageing_conversions.put(new ResourceLocation(from), new AgeErosionConversion(from, to, requiredAge));
		} else {
			String message = "";
			boolean flag = false;
			if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from))) message = from;
			if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
				if (message.isEmpty()) message = to;
				else {
					message = from + " and " + to;
					flag = true;
				}
			}
			BurdenOfTime.getInstance().getLogger().warn("The Block"+(flag?"s ":" ")+message+" does not currently exist. If "+(flag?"these blocks are ":"this block is ")+" from another mod, "
					+ "then install that mod for this age conversion to work. If the required mod is already installed and you get this message, then report this issue to the mod developer");
		}
	}

	public static Map<ResourceLocation, List<FluidErosionConversion>> fluid_conversions = Maps.newHashMap();

	public static void registerErosionConversions(String from, String to, int requiredAge, String... requiredFluids) {
		if (ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from)) && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
			List<FluidErosionConversion> list = Lists.newArrayList();
			for (String requiredFluid : requiredFluids) {
				if (ForgeRegistries.FLUIDS.containsKey(new ResourceLocation(requiredFluid))) {
					list.add(new FluidErosionConversion(from, to, requiredAge, requiredFluid));
				}
			}
			fluid_conversions.put(new ResourceLocation(from), list);
		} else {
			String message = "";
			boolean flag = false;
			if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from))) message = from;
			if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
				if (message.isEmpty()) message = to;
				else {
					message = from + " and " + to;
					flag = true;
				}
			}
			BurdenOfTime.getInstance().getLogger().warn("The Block"+(flag?"s ":" ")+message+" does not currently exist. If "+(flag?"these blocks are ":"this block is ")+" from another mod, "
					+ "then install that mod for this age conversion to work. If the required mod is already installed and you get this message, then report this issue to the mod developer");
		}
	}

	public static void regeisterAllConversions() {
		

//		registerTrampleConversions("minecraft:grass_block", "minecraft:dirt", 10f);
//		registerTrampleConversions("minecraft:dirt", "minecraft:coarse_dirt", 10f);
//		registerTrampleConversions("minecraft:coarse_dirt", "minecraft:grass_path", 10f);
		
//		registerAgeConversions("minecraft:cobblestone_slab", "minecraft:mossy_cobblestone_slab", 10);
//		registerAgeConversions("minecraft:cobblestone_stairs", "minecraft:mossy_cobblestone_stairs", 10);
//		registerAgeConversions("minecraft:cobblestone_wall", "minecraft:mossy_cobblestone_wall", 10);
//		registerAgeConversions("minecraft:stone_bricks", "minecraft:mossy_stone_bricks", 10);
//		registerAgeConversions("minecraft:stone_brick_slab", "minecraft:mossy_stone_brick_slab", 10);
//		registerAgeConversions("minecraft:stone_brick_stairs", "minecraft:mossy_stone_brick_stairs", 10);
//		registerAgeConversions("minecraft:stone_brick_wall", "minecraft:mossy_stone_brick_wall", 10);
		//Create
//		registerAgeConversions("create:granite_bricks", "create:mossy_granite", 10);
//		registerAgeConversions("create:mossy_granite", "create:overgrown_granite", 10);
//		registerAgeConversions("create:diorite_bricks", "create:mossy_diorite", 10);
//		registerAgeConversions("create:mossy_diorite", "create:overgrown_diorite", 10);
//		registerAgeConversions("create:andesite_bricks", "create:mossy_andesite", 10);
//		registerAgeConversions("create:mossy_andesite", "create:overgrown_andesite", 10);
		registerAgeConversions("create:limestone_bricks", "create:mossy_limestone", 10);
		registerAgeConversions("create:mossy_limestone", "create:overgrown_limestone", 10);
		registerAgeConversions("create:weathered_limestone_bricks", "create:mossy_weathered_limestone", 10);
		registerAgeConversions("create:mossy_weathered_limestone", "create:overgrown_weathered_limestone", 10);
		registerAgeConversions("create:dolomite_bricks", "create:mossy_dolomite", 10);
		registerAgeConversions("create:mossy_dolomite", "create:overgrown_dolomite", 10);
		registerAgeConversions("create:gabbro_bricks", "create:mossy_gabbro", 10);
		registerAgeConversions("create:mossy_gabbro", "create:overgrown_gabbro", 10);
		registerAgeConversions("create:scoria_bricks", "create:mossy_scoria", 10);
		registerAgeConversions("create:mossy_scoria", "create:overgrown_scoria", 10);
		registerAgeConversions("create:dark_scoria_bricks", "create:mossy_dark_scoria", 10);
		registerAgeConversions("create:mossy_dark_scoria", "create:overgrown_dark_scoria", 10);
		//Ice And Fire
		registerAgeConversions("iceandfire:dread_stone_bricks", "iceandfire:dread_stone_bricks_mossy", 10);
		//Quark
		registerAgeConversions("minecraft:cobblestone_bricks", "minecraft:mossy_cobblestone_bricks", 10);

		//
		registerErosionConversions("minecraft:stone", "minecraft:cobblestone", 5, "minecraft:flowing_water");
		registerErosionConversions("minecraft:stone_slab", "minecraft:cobblestone_slab", 5, "minecraft:flowing_water");
		registerErosionConversions("minecraft:stone_stairs", "minecraft:cobblestone_stairs", 5, "minecraft:flowing_water");
		registerErosionConversions("minecraft:stone_bricks", "minecraft:cracked_stone_bricks", 5, "minecraft:flowing_water");
//		registerErosionConversions("minecraft:polished_blackstone_bricks", "minecraft:cracked_polished_blackstone_bricks", 5, "minecraft:flowing_water");
//		registerErosionConversions("minecraft:nether_bricks", "minecraft:cracked_polished_nether_bricks", 5, "minecraft:flowing_water");
		
		registerErosionConversions("minecraft:light_gray_terracotta", "minecraft:light_gray_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:black_terracotta", "minecraft:black_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:magenta_terracotta", "minecraft:magenta_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:light_blue_terracotta", "minecraft:light_blue_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:purple_terracotta", "minecraft:purple_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:pink_terracotta", "minecraft:pink_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:brown_terracotta", "minecraft:brown_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:white_terracotta", "minecraft:white_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:red_terracotta", "minecraft:red_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:yellow_terracotta", "minecraft:yellow_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:cyan_terracotta", "minecraft:cyan_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:green_terracotta", "minecraft:green_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:orange_terracotta", "minecraft:orange_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:gray_terracotta", "minecraft:gray_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:lime_terracotta", "minecraft:lime_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:blue_terracotta", "minecraft:blue_glazed_terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:quartz_block", "minecraft:smooth_quartz", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:sandstone", "minecraft:smooth_sandstone", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:wet_sponge", "minecraft:sponge", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:stone_bricks", "minecraft:cracked_stone_bricks", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:stone", "minecraft:smooth_stone", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:sand", "minecraft:glass", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:red_sand", "minecraft:glass", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:clay", "minecraft:terracotta", 4, "minecraft:flowing_lava", "minecraft:lava");
		registerErosionConversions("minecraft:red_sandstone", "minecraft:smooth_red_sandstone", 4, "minecraft:flowing_lava", "minecraft:lava");
//		registerErosionConversions("minecraft:polished_blackstone_bricks", "minecraft:cracked_polished_blackstone_bricks", 4, "minecraft:flowing_lava", "minecraft:lava");
//		registerErosionConversions("minecraft:nether_bricks", "minecraft:cracked_polished_nether_bricks", 4, "minecraft:flowing_lava", "minecraft:lava");
	}
}
