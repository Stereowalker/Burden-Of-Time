package com.stereowalker.burdenoftime.conversions;

import net.minecraft.block.Block;

public class TrampleConversion extends Conversion
{
    public float requiredDepth;

    public TrampleConversion(Block from, Block to, float requiredDepth)
    {
    	super(from, to);
        this.requiredDepth = requiredDepth;
    }

    public TrampleConversion(String from, String to, float requiredDepth)
    {
    	super(from, to);
        this.requiredDepth = requiredDepth;
    }
}
