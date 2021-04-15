package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.stereowalker.burdenoftime.BurdenOfTime;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class ErosionMap extends WorldSavedData
{
    public static final String KEY = BurdenOfTime.instance.getModid() + "erosion_map";

    public HashMap<BlockPos, Float> erosionMap = new HashMap<>();
    private Gson gson;

    private ErosionMap()
    {
        super(KEY);
        gson = new Gson();
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        for (BlockPos entry : erosionMap.keySet())
        {
            float depth = erosionMap.get(entry);
            tag.putFloat(gson.toJson(entry), depth);
        }
        return tag;
    }

    @Override
    public void read(CompoundNBT tag)
    {
        erosionMap.clear();

        for (String entry : tag.keySet())
        {
            float depth = tag.getFloat(entry);
            BlockPos pos = gson.fromJson(entry, BlockPos.class);

            erosionMap.put(pos, depth);
        }

    }

    public static ErosionMap getInstance(MinecraftServer server, RegistryKey<World> dimension)
    {
    	DimensionSavedDataManager manager = Objects.requireNonNull(server.getWorld(dimension)).getSavedData();
        return manager.getOrCreate(ErosionMap::new, KEY);
    }
}
