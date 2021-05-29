package com.stereowalker.burdenoftime.conversions;

import net.minecraft.block.Block;

public class AgeErosionConversion extends Conversion
{
    public int requiredAge;

    public AgeErosionConversion(Block from, Block to, int requiredAge)
    {
    	super(from, to);
        this.requiredAge = requiredAge;
    }

    public AgeErosionConversion(String from, String to, int requiredAge)
    {
    	super(from, to);
        this.requiredAge = requiredAge;
    }
}
