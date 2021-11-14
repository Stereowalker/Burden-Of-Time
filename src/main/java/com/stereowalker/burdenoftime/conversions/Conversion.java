package com.stereowalker.burdenoftime.conversions;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
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
        ResourceLocation fromBlock = ResourceLocation.tryParse(from);
        ResourceLocation toBlock = ResourceLocation.tryParse(to);

        if (fromBlock == null || toBlock == null)
            throw new ResourceLocationException("An invalid pathlink has been detected: {" + from + ", " + to + "}");

        this.from = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(from));
        this.to = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(to));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void handleConversion(Level world, BlockPos pos, BlockState old, float counter, float requiredNumber) {
    	if (this.from == old.getBlock() && counter > requiredNumber)
		{
			BlockState convertedState = this.to.defaultBlockState();
			for (Property property : old.getProperties()) {
				if (convertedState.hasProperty(property))
					convertedState = convertedState.setValue(property, old.getValue(property));
			}
			world.setBlock(pos, convertedState, 11);
		}
    }
}
