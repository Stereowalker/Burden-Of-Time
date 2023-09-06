package com.stereowalker.burdenoftime.resource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.stereowalker.burdenoftime.BurdenOfTime;
import com.stereowalker.burdenoftime.conversions.AgeErosionConversion;
import com.stereowalker.burdenoftime.conversions.Conversion;
import com.stereowalker.burdenoftime.conversions.Conversions;
import com.stereowalker.burdenoftime.conversions.FluidErosionConversion;
import com.stereowalker.burdenoftime.conversions.TrampleErosionConversion;
import com.stereowalker.unionlib.resource.IResourceReloadListener;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Loads block temperatures from json
 * @author Stereowalker
 */
public class ConversionDataManager implements IResourceReloadListener<List<Conversion>> {
	@Override
	public CompletableFuture<List<Conversion>> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			List<Conversion> conversionMap = new ArrayList<>();

			for (ResourceLocation id : manager.listResources("block_conversions", (s) -> s.endsWith(".json"))) {
				ResourceLocation blockId = new ResourceLocation(
						id.getNamespace(),
						id.getPath().replace("block_conversions/", "").replace(".json", "")
						);
				if (ForgeRegistries.BLOCKS.containsKey(blockId)) {
					try {
						Resource resource = manager.getResource(id);
						try (InputStream stream = resource.getInputStream(); 
								InputStreamReader reader = new InputStreamReader(stream)) {
							
							JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
							
							if(object.entrySet().size() != 0) {
								//Trample
								if (object.has("trample_conversion") && object.get("trample_conversion").isJsonObject()) {
									JsonObject trampleConversion = object.get("trample_conversion").getAsJsonObject();
									String to = "";
									float depth = 0;
									boolean flag = true;
									if (trampleConversion.has("to") && trampleConversion.get("to").isJsonPrimitive()) {
										to = trampleConversion.get("to").getAsString();
									} else flag = false;
									if (trampleConversion.has("depth") && trampleConversion.get("depth").isJsonPrimitive()) {
										depth = trampleConversion.get("depth").getAsFloat();
									} else flag = false;
									
									if (depth <= 0) {
										flag = false;
										BurdenOfTime.getInstance().getLogger().info("The required depth for the trampleConversion conversion of \""+blockId+"\" is less than or equal to zero");
									}
									if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
										flag = false;
										BurdenOfTime.getInstance().getLogger().info("The conversion block for the age conversion of \""+blockId+"\" does not exist");
									}
									if (flag) {
										conversionMap.add(new TrampleErosionConversion(blockId.toString(), to, depth));
										BurdenOfTime.getInstance().getLogger().info("Queued trample conversion of \""+blockId+"\" for registration");
									}
								}
								//Age
								if (object.has("age_conversion") && object.get("age_conversion").isJsonObject()) {
									JsonObject ageConversion = object.get("age_conversion").getAsJsonObject();
									String to = "";
									int age = 0;
									boolean flag = true;
									if (ageConversion.has("to") && ageConversion.get("to").isJsonPrimitive()) {
										to = ageConversion.get("to").getAsString();
									} else flag = false;
									if (ageConversion.has("age") && ageConversion.get("age").isJsonPrimitive()) {
										age = ageConversion.get("age").getAsInt();
									} else flag = false;
									
									if (age <= 0) {
										flag = false;
										BurdenOfTime.getInstance().getLogger().info("The required age for the age conversion of \""+blockId+"\" is less than or equal to zero");
									}
									if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
										flag = false;
										BurdenOfTime.getInstance().getLogger().info("The conversion block for the age conversion of \""+blockId+"\" does not exist");
									}
									if (flag) {
										conversionMap.add(new AgeErosionConversion(blockId.toString(), to, age));
										BurdenOfTime.getInstance().getLogger().info("Queued age conversion of \""+blockId+"\" for registration");
									}
								}
								//Fluid
								if (object.has("fluid_conversion") && object.get("fluid_conversion").isJsonObject()) {
									JsonObject fluidConversion = object.get("fluid_conversion").getAsJsonObject();
									String to = "";
									List<Pair<String, Integer>> fluids = new ArrayList<>();
									boolean flag = true;
									if (fluidConversion.has("to") && fluidConversion.get("to").isJsonPrimitive()) {
										to = fluidConversion.get("to").getAsString();
									} else flag = false;
									if (fluidConversion.has("fluids") && fluidConversion.get("fluids").isJsonArray()) {
										for (JsonElement elem : fluidConversion.get("fluids").getAsJsonArray()) {
											boolean flag2 = true;
											if (elem.getAsJsonObject().has("id") && elem.getAsJsonObject().get("id").isJsonPrimitive()) {
												if (elem.getAsJsonObject().has("age") && elem.getAsJsonObject().get("age").isJsonPrimitive()) {
												} else {
													BurdenOfTime.getInstance().getLogger().info("The required age for the fluid conversion of \""+blockId+"\" with \""+elem.getAsJsonObject().get("id").getAsString()+"\"is undefined");
													flag2 = false;
												}
											} else {
												BurdenOfTime.getInstance().getLogger().info("The fluid for the fluid conversion of \""+blockId+"\" was not defined");
												flag2 = false;
											}
											if (elem.isJsonObject() && flag2) {
												fluids.add(Pair.of(elem.getAsJsonObject().get("id").getAsString(), elem.getAsJsonObject().get("age").getAsInt()));
											}
										}
										//BurdenOfTime.getInstance().getLogger().info("FDDDDDEESS "+ i);
									} else flag = false;
									
									
									if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(to))) {
										flag = false;
										BurdenOfTime.getInstance().getLogger().info("The conversion block for the fluid conversion of \""+blockId+"\" does not exist");
									}
									for (Pair<String, Integer> fluid : fluids) {
										if (!ForgeRegistries.FLUIDS.containsKey(new ResourceLocation(fluid.getFirst()))) {
											flag = false;
											BurdenOfTime.getInstance().getLogger().info("The \""+fluid.getFirst()+"\" fluid for the fluid conversion of \""+blockId+"\" does not exist");
										}
										if (fluid.getSecond() <= 0) {
											flag = false;
											BurdenOfTime.getInstance().getLogger().info("The required age for the fluid conversion of \""+blockId+"\" with \""+fluid.getFirst()+"\"is less than or equal to zero");
										}
									}
									if (flag) {
										for (Pair<String, Integer> fluid : fluids) {
											conversionMap.add(new FluidErosionConversion(blockId.toString(), to, fluid.getSecond(), fluid.getFirst()));
										}
										BurdenOfTime.getInstance().getLogger().info("Queued fluid conversion of \""+blockId+"\" for registration");
									}
								}
							}
						}
					} catch (Exception e) {
						BurdenOfTime.getInstance().getLogger().warn("Error reading the block conversion \""+blockId+"\" !", e);
					}
				} else {
					BurdenOfTime.getInstance().getLogger().warn("No such block exists with the id  \""+blockId+"\" !");
				}
			}

			return conversionMap;
		});
	}

	@Override
	public CompletableFuture<Void> apply(List<Conversion> data, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			for (Conversion conversion : data) {
				if (conversion instanceof TrampleErosionConversion) {
					Conversions.registerTrampleConversions(((TrampleErosionConversion)conversion).from.getRegistryName().toString(), ((TrampleErosionConversion)conversion).to.getRegistryName().toString(), ((TrampleErosionConversion)conversion).requiredDepth);
				}
				if (conversion instanceof AgeErosionConversion) {
					Conversions.registerAgeConversions(((AgeErosionConversion)conversion).from.getRegistryName().toString(), ((AgeErosionConversion)conversion).to.getRegistryName().toString(), ((AgeErosionConversion)conversion).requiredAge);
				}
				if (conversion instanceof FluidErosionConversion) {
					Conversions.registerErosionConversions(((AgeErosionConversion)conversion).from.getRegistryName().toString(), ((AgeErosionConversion)conversion).to.getRegistryName().toString(), ((AgeErosionConversion)conversion).requiredAge);
				}
			}
		}, executor);
	}
}
