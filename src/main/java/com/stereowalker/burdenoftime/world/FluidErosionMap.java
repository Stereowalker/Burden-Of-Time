package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.stereowalker.burdenoftime.BurdenOfTime;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidErosionMap extends WorldSavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "fluid_map";

    public HashMap<BlockPos, HashMap<Fluid, Integer>> wearMap = new HashMap<>();
    private Gson gson;

    private FluidErosionMap()
    {
        super(KEY);
        gson = new Gson();
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        for (BlockPos entry : wearMap.keySet())
        {
        	HashMap<Fluid, Integer> fluidMap = wearMap.getOrDefault(entry, new HashMap<>());
        	for (Fluid fluid : fluidMap.keySet()) 
        	{
        		int age = wearMap.get(entry).get(fluid);
        		tag.putInt(gson.toJson(entry)+"#"+fluid.getRegistryName(), age);
        	}
        }
        return tag;
    }

    @Override
    public void read(CompoundNBT tag)
    {
    	wearMap.clear();

        for (String entry : tag.keySet())
        {
        	String[] entries = entry.split("#");
            int age = tag.getInt(entry);
            BlockPos pos = gson.fromJson(entries[0], BlockPos.class);

            HashMap<Fluid, Integer> fluidMap = wearMap.getOrDefault(entries[0], new HashMap<>());
            fluidMap.put(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(entries[1])), age);
            wearMap.put(pos, fluidMap);
        }

    }

    public static FluidErosionMap getInstance(MinecraftServer server, RegistryKey<World> dimension)
    {
    	DimensionSavedDataManager manager = Objects.requireNonNull(server.getWorld(dimension)).getSavedData();
        return manager.getOrCreate(FluidErosionMap::new, KEY);
    }
}
