package com.stereowalker.burdenoftime.conversions;

import net.minecraft.block.Block;

public class AgeConversion extends Conversion
{
    public int requiredAge;

    public AgeConversion(Block from, Block to, int requiredAge)
    {
    	super(from, to);
        this.requiredAge = requiredAge;
    }

    public AgeConversion(String from, String to, int requiredAge)
    {
    	super(from, to);
        this.requiredAge = requiredAge;
    }
}
