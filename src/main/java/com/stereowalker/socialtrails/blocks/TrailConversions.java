package com.stereowalker.socialtrails.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.registries.ForgeRegistries;

public class TrailConversions
{
    public Block from;
    public Block to;
    public float requiredDepth;

    public TrailConversions(Block from, Block to, float requiredDepth)
    {
        this.from = from;
        this.to = to;
        this.requiredDepth = requiredDepth;
    }

    public TrailConversions(String from, String to, float requiredDepth)
    {
        ResourceLocation fromBlock = ResourceLocation.tryCreate(from);
        ResourceLocation toBlock = ResourceLocation.tryCreate(to);
        this.requiredDepth = requiredDepth;

        if (fromBlock == null || toBlock == null)
            throw new ResourceLocationException("An invalid pathlink has been detected: {" + from + ", " + to + "}");

        this.from = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(from));
        this.to = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(to));
    }
}
