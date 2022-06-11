package com.stereowalker.burdenoftime.conversions;

import com.stereowalker.burdenoftime.config.Config;

import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
			final Block b = convertedState.getBlock();
			if (Config.sendToChat)
				world.players().forEach((p) -> 
				p.sendMessage(new TextComponent("Converting "+old.getBlock()+" to "+b+" at ")
						.append(new TextComponent("{"+pos.getX()+" "+pos.getY()+" "+pos.getZ()+"}").withStyle((p_207527_) -> {
					return p_207527_.withColor(ChatFormatting.GREEN)
							.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + pos.getX()+" "+pos.getY()+" "+pos.getZ()))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip")));
				})), Util.NIL_UUID));
			world.setBlock(pos, convertedState, 11);
		}
	}
}
