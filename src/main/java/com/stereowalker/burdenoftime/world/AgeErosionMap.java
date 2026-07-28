package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.stereowalker.burdenoftime.BurdenOfTime;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

public class AgeErosionMap extends SavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "age_map";
    
    public static final Codec<AgeErosionMap> CODEC = RecordCodecBuilder.create(instance -> 
    instance.group(Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("age_map").forGetter(data -> {
    	HashMap<String, Integer> mapForSaving = new HashMap<>();
    	for (var entry : data.ageMap.entrySet()) {
    		if (entry.getValue() != null) {
    			mapForSaving.put(data.gson.toJson(entry.getKey()), entry.getValue());
    		}
    	}
    	return mapForSaving;
    })).apply(instance, (stringMap) -> {
    	AgeErosionMap data = new AgeErosionMap();
    	for (var entry : stringMap.entrySet()) {
    		BlockPos pos = data.gson.fromJson(entry.getKey(), BlockPos.class);
    		data.ageMap.put(pos, entry.getValue());
    	}
    	return data;
    }));
    public static final SavedDataType<AgeErosionMap> TYPE = new SavedDataType<>(VersionHelper.toLoc(BurdenOfTime.ID, "age_map"), AgeErosionMap::new, CODEC, DataFixTypes.SAVED_DATA_MAP_INDEX);

    public HashMap<BlockPos, Integer> ageMap = new HashMap<>();
    private Gson gson;

    private AgeErosionMap()
    {
        super(/*KEY*/);
        gson = new Gson();
    }

    public static AgeErosionMap getInstance(MinecraftServer server, ResourceKey<Level> dimension)
    {
    	SavedDataStorage manager = Objects.requireNonNull(server.getLevel(dimension)).getDataStorage();
        return manager.computeIfAbsent(TYPE);
    }
}
