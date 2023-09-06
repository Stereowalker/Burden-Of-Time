package com.stereowalker.burdenoftime.conversions;

import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class FluidErosionConversion extends Conversion
{
    public int requiredAge;
    public Fluid requiredFluid;

    public FluidErosionConversion(Block from, Block to, int requiredAge, Fluid requiredFluid)
    {
    	super(from, to);
        this.requiredAge = requiredAge;
        this.requiredFluid = requiredFluid;
    }

    public FluidErosionConversion(String from, String to, int requiredAge, String requiredFluid)
    {
    	super(from, to);
        this.requiredAge = requiredAge;
        ResourceLocation requiredFluidL = ResourceLocation.tryParse(requiredFluid);

        if (requiredFluidL == null)
            throw new ResourceLocationException("An invalid fluid has been detected: {" + requiredFluid + "}");

        this.requiredFluid = RegistryHelper.getFluid(ResourceLocation.tryParse(requiredFluid));
    }
}
