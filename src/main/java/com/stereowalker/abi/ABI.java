package com.stereowalker.abi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stereowalker.unionlib.mod.MinecraftMod;

import net.minecraft.resources.ResourceLocation;

public class ABI extends MinecraftMod
{
	public static ABI instance;
	public static final String MOD_ID = "abi";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public ABI() 
	{
		super(MOD_ID, new ResourceLocation(MOD_ID, "textures/mod.png"), LoadType.BOTH);
		instance = this;
	}

	public static ABI getInstance() {
		return instance;
	}

	public static void debug(String message) {
		LOGGER.info(message);
	}

	public ResourceLocation location(String name)
	{
		return new ResourceLocation(MOD_ID, name);
	}

	public static class Locations {
	}
}
