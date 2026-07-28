package com.stereowalker.burdenoftime.conversions;

import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.IdentifierException;
import net.minecraft.resources.Identifier;
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
        Identifier requiredFluidL = Identifier.tryParse(requiredFluid);

        if (requiredFluidL == null)
            throw new IdentifierException("An invalid fluid has been detected: {" + requiredFluid + "}");

        this.requiredFluid = RegistryHelper.getFluid(Identifier.tryParse(requiredFluid));
    }
}
