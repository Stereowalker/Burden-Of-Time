package com.stereowalker.burdenoftime.conversions;

import net.minecraft.world.level.block.Block;

public class TrampleErosionConversion extends Conversion
{
    public float requiredDepth;

    public TrampleErosionConversion(Block from, Block to, float requiredDepth)
    {
    	super(from, to);
        this.requiredDepth = requiredDepth;
    }

    public TrampleErosionConversion(String from, String to, float requiredDepth)
    {
    	super(from, to);
        this.requiredDepth = requiredDepth;
    }
}
