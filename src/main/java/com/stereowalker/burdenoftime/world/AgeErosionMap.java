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

public class AgeErosionMap extends WorldSavedData
{
    public static final String KEY = BurdenOfTime.instance.getModid() + "age_map";

    public HashMap<BlockPos, Integer> ageMap = new HashMap<>();
    private Gson gson;

    private AgeErosionMap()
    {
        super(KEY);
        gson = new Gson();
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        for (BlockPos entry : ageMap.keySet())
        {
            int age = ageMap.get(entry);
            tag.putInt(gson.toJson(entry), age);
        }
        return tag;
    }

    @Override
    public void read(CompoundNBT tag)
    {
        ageMap.clear();

        for (String entry : tag.keySet())
        {
            int age = tag.getInt(entry);
            BlockPos pos = gson.fromJson(entry, BlockPos.class);

            ageMap.put(pos, age);
        }

    }

    public static AgeErosionMap getInstance(MinecraftServer server, RegistryKey<World> dimension)
    {
    	DimensionSavedDataManager manager = Objects.requireNonNull(server.getWorld(dimension)).getSavedData();
        return manager.getOrCreate(AgeErosionMap::new, KEY);
    }
}
