package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.stereowalker.burdenoftime.BurdenOfTime;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

public class TrampleErosionMap extends SavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "erosion_map";
    
    public static final Codec<TrampleErosionMap> CODEC = RecordCodecBuilder.create(instance -> 
    instance.group(Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("erosion_map").forGetter(data -> {
    	HashMap<String, Float> mapForSaving = new HashMap<>();
    	for (var entry : data.erosionMap.entrySet()) {
    		if (entry.getValue() != null) {
    			mapForSaving.put(data.gson.toJson(entry.getKey()), entry.getValue());
    		}
    	}
    	return mapForSaving;
    })).apply(instance, (stringMap) -> {
    	TrampleErosionMap data = new TrampleErosionMap();
    	for (var entry : stringMap.entrySet()) {
    		BlockPos pos = data.gson.fromJson(entry.getKey(), BlockPos.class);
    		data.erosionMap.put(pos, entry.getValue());
    	}
    	return data;
    }));
    public static final SavedDataType<TrampleErosionMap> TYPE = new SavedDataType<>(VersionHelper.toLoc(BurdenOfTime.ID, "erosion_map"), TrampleErosionMap::new, CODEC, DataFixTypes.SAVED_DATA_MAP_INDEX);

    public HashMap<BlockPos, Float> erosionMap = new HashMap<>();
    private Gson gson;

    private TrampleErosionMap()
    {
        super(/*KEY*/);
        gson = new Gson();
    }

    public static TrampleErosionMap getInstance(MinecraftServer server, ResourceKey<Level> dimension)
    {
    	SavedDataStorage manager = Objects.requireNonNull(server.getLevel(dimension)).getDataStorage();
        return manager.computeIfAbsent(TYPE);
    }
}
