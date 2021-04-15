package com.stereowalker.burdenoftime.conversions;

import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.burdenoftime.BurdenOfTime;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Conversions {
	public static TrampleConversion[] trample_conversions = new TrampleConversion[]{
			new TrampleConversion("minecraft:grass_block", "minecraft:dirt", 10f),
			new TrampleConversion("minecraft:dirt", "minecraft:coarse_dirt", 10f),
			new TrampleConversion("minecraft:coarse_dirt", "minecraft:grass_path", 10f),
	};

	public static List<AgeConversion> ageing_conversions = Lists.newArrayList();

	private static void registerAgeConversions(String from, String to, int requiredAge) {
		if (ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(from)) && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
			ageing_conversions.add(new AgeConversion(from, to, requiredAge));
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
			BurdenOfTime.instance.LOGGER.warn("The Block"+(flag?"s ":" ")+message+" does not currently exist. If "+(flag?"these blocks are ":"this block is ")+" from another mod, "
					+ "then install that mod for this age conversion to work. If the required mod is already installed and you get this message, then report this issue to the mod developer");
		}
	}
	
	public static void regeisterAllConversions() {
		registerAgeConversions("minecraft:cobblestone", "minecraft:mossy_cobblestone", 10);
		registerAgeConversions("minecraft:cobblestone_slab", "minecraft:mossy_cobblestone_slab", 10);
		registerAgeConversions("minecraft:cobblestone_stairs", "minecraft:mossy_cobblestone_stairs", 10);
		registerAgeConversions("minecraft:cobblestone_wall", "minecraft:mossy_cobblestone_wall", 10);
		registerAgeConversions("minecraft:stone_bricks", "minecraft:mossy_stone_bricks", 10);
		registerAgeConversions("minecraft:stone_brick_slab", "minecraft:mossy_stone_brick_slab", 10);
		registerAgeConversions("minecraft:stone_brick_stairs", "minecraft:mossy_stone_brick_stairs", 10);
		registerAgeConversions("minecraft:stone_brick_wall", "minecraft:mossy_stone_brick_wall", 10);
		registerAgeConversions("minecraft:grass_block", "minecraft:cobblestone", 10);
		registerAgeConversions("minecraft:sand", "minecraft:ice", 10);
	}
}
