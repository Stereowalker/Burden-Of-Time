package com.stereowalker.burdenoftime.world;

import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.stereowalker.burdenoftime.BurdenOfTime;
import com.stereowalker.unionlib.util.RegistryHelper;
import com.stereowalker.unionlib.util.VersionHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

public class FluidErosionMap extends SavedData
{
    public static final String KEY = BurdenOfTime.getInstance().getModid() + "fluid_map";
    
    private static final Codec<Fluid> FLUID_CODEC = Codec.STRING.xmap(str -> RegistryHelper.getFluid(VersionHelper.toLoc(str)), fluid -> RegistryHelper.getFluidKey(fluid).toString());

    // 2. Define the Codec for the entire FluidErosionMap object
    public static final Codec<FluidErosionMap> CODEC = RecordCodecBuilder.create(instance -> 
    instance.group(Codec.unboundedMap(BlockPos.CODEC, Codec.unboundedMap(FLUID_CODEC, Codec.INT).xmap(HashMap::new, map -> map)).fieldOf("wear_map").forGetter(data -> data.wearMap)).apply(instance, (wearMap) -> {
    			FluidErosionMap data = new FluidErosionMap();
    			data.wearMap.putAll(wearMap); 
    			return data;
    		}));
    public static final SavedDataType<FluidErosionMap> TYPE = new SavedDataType<>(VersionHelper.toLoc(BurdenOfTime.ID, "wear_map"), FluidErosionMap::new, CODEC, DataFixTypes.SAVED_DATA_MAP_INDEX);

    public HashMap<BlockPos, HashMap<Fluid, Integer>> wearMap = new HashMap<>();
    private Gson gson;

    private FluidErosionMap()
    {
        super(/*KEY*/);
        gson = new Gson();
    }

    public static FluidErosionMap getInstance(MinecraftServer server, ResourceKey<Level> dimension)
    {
    	SavedDataStorage manager = Objects.requireNonNull(server.getLevel(dimension)).getDataStorage();
        return manager.computeIfAbsent(TYPE);
    }
}
