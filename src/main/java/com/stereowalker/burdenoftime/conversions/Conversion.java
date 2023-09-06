package com.stereowalker.burdenoftime.conversions;

import com.stereowalker.burdenoftime.config.Config;
import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

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

		this.from = RegistryHelper.getBlock(ResourceLocation.tryParse(from));
		this.to = RegistryHelper.getBlock(ResourceLocation.tryParse(to));
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
			final Block b = convertedState.getBlock();
			if (Config.sendToChat)
				world.players().forEach((p) -> 
				p.sendSystemMessage(Component.literal("Converting "+old.getBlock()+" to "+b+" at ")
						.append(Component.literal("{"+pos.getX()+" "+pos.getY()+" "+pos.getZ()+"}").withStyle((p_207527_) -> {
					return p_207527_.withColor(ChatFormatting.GREEN)
							.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + pos.getX()+" "+pos.getY()+" "+pos.getZ()))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip")));
				}))));
			world.setBlock(pos, convertedState, 11);
		}
	}
}
