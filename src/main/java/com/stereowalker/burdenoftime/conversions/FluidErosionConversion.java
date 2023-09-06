package com.stereowalker.burdenoftime.conversions;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

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

        this.requiredFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(requiredFluid));
    }
}
