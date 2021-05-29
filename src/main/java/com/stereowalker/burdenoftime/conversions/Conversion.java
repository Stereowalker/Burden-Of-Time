package com.stereowalker.burdenoftime.conversions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void handleConversion(World world, BlockPos pos, BlockState old, float counter, float requiredNumber) {
    	if (this.from == old.getBlock() && counter > requiredNumber)
		{
			BlockState convertedState = this.to.getDefaultState();
			for (Property property : old.getProperties()) {
				if (convertedState.hasProperty(property))
					convertedState = convertedState.with(property, old.get(property));
			}
			world.setBlockState(pos, convertedState, 11);
		}
    }
}
