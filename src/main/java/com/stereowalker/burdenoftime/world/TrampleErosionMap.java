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

public class TrampleErosionMap extends SavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "erosion_map";

    public HashMap<BlockPos, Float> erosionMap = new HashMap<>();
    private Gson gson;

    private TrampleErosionMap()
    {
        super(/*KEY*/);
        gson = new Gson();
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        for (BlockPos entry : erosionMap.keySet())
        {
            float depth = erosionMap.get(entry);
            tag.putFloat(gson.toJson(entry), depth);
        }
        return tag;
    }

    public static TrampleErosionMap read(CompoundTag tag)
    {
    	TrampleErosionMap map = new TrampleErosionMap();
        map.erosionMap.clear();

        for (String entry : tag.getAllKeys())
        {
            float depth = tag.getFloat(entry);
            BlockPos pos = map.gson.fromJson(entry, BlockPos.class);

            map.erosionMap.put(pos, depth);
        }
        return map;
    }

    public static TrampleErosionMap getInstance(MinecraftServer server, ResourceKey<Level> dimension)
    {
    	DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(dimension)).getDataStorage();
        return manager.computeIfAbsent(TrampleErosionMap::read, TrampleErosionMap::new, KEY);
    }
}
