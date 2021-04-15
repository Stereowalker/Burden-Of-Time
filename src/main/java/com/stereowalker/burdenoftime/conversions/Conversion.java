package com.stereowalker.burdenoftime.conversions;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.registries.ForgeRegistries;

public class Conversion
{
    public Block from;
    public Block to;

    public Conversion(Block from, Block to)
    {
        this.from = from;
        this.to = to;
    }

    public Conversion(String from, String to)
    {
        ResourceLocation fromBlock = ResourceLocation.tryCreate(from);
        ResourceLocation toBlock = ResourceLocation.tryCreate(to);

        if (fromBlock == null || toBlock == null)
            throw new ResourceLocationException("An invalid pathlink has been detected: {" + from + ", " + to + "}");

        this.from = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(from));
        this.to = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(to));
    }
}
