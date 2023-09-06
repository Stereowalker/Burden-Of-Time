package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.stereowalker.burdenoftime.BurdenOfTime;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class AgeErosionMap extends SavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "age_map";

    public HashMap<BlockPos, Integer> ageMap = new HashMap<>();
    private Gson gson;

    private AgeErosionMap()
    {
        super(/*KEY*/);
        gson = new Gson();
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        for (BlockPos entry : ageMap.keySet())
        {
            int age = ageMap.get(entry);
            tag.putInt(gson.toJson(entry), age);
        }
        return tag;
    }

    public static AgeErosionMap read(CompoundTag tag)
    {
    	AgeErosionMap map = new AgeErosionMap();
        map.ageMap.clear();

        for (String entry : tag.getAllKeys())
        {
            int age = tag.getInt(entry);
            BlockPos pos = map.gson.fromJson(entry, BlockPos.class);

            map.ageMap.put(pos, age);
        }
        return map;

    }

    public static AgeErosionMap getInstance(MinecraftServer server, ResourceKey<Level> dimension)
    {
    	DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(dimension)).getDataStorage();
        return manager.computeIfAbsent(AgeErosionMap::read, AgeErosionMap::new, KEY);
    }
}
