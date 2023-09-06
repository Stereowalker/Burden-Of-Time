package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.stereowalker.burdenoftime.BurdenOfTime;
import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidErosionMap extends SavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "fluid_map";

    public HashMap<BlockPos, HashMap<Fluid, Integer>> wearMap = new HashMap<>();
    private Gson gson;

    private FluidErosionMap()
    {
        super(/*KEY*/);
        gson = new Gson();
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        for (BlockPos entry : wearMap.keySet())
        {
        	HashMap<Fluid, Integer> fluidMap = wearMap.getOrDefault(entry, new HashMap<>());
        	for (Fluid fluid : fluidMap.keySet()) 
        	{
        		int age = wearMap.get(entry).get(fluid);
        		tag.putInt(gson.toJson(entry)+"#"+RegistryHelper.getFluidKey(fluid), age);
        	}
        }
        return tag;
    }

    public static FluidErosionMap read(CompoundTag tag)
    {
    	FluidErosionMap map = new FluidErosionMap();
    	map.wearMap.clear();

        for (String entry : tag.getAllKeys())
        {
        	String[] entries = entry.split("#");
            int age = tag.getInt(entry);
            BlockPos pos = map.gson.fromJson(entries[0], BlockPos.class);

            HashMap<Fluid, Integer> fluidMap = map.wearMap.getOrDefault(entries[0], new HashMap<>());
            fluidMap.put(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(entries[1])), age);
            map.wearMap.put(pos, fluidMap);
        }
        return map;
    }

    public static FluidErosionMap getInstance(MinecraftServer server, ResourceKey<Level> dimension)
    {
    	DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(dimension)).getDataStorage();
        return manager.computeIfAbsent(FluidErosionMap::read, FluidErosionMap::new, KEY);
    }
}
