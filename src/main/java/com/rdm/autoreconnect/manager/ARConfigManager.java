package com.rdm.autoreconnect.manager;

import org.apache.commons.lang3.tuple.Pair;

import com.rdm.autoreconnect.config.ARConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ARConfigManager {
	public static final ForgeConfigSpec MAIN_CLIENT_SPEC;
	public static final ARConfig MAIN_CLIENT;
	
	static {
		final Pair<ARConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ARConfig::new);
		
		MAIN_CLIENT_SPEC = clientSpecPair.getRight();
		MAIN_CLIENT = clientSpecPair.getLeft();
	}
	
	public static void registerConfigs() {
		registerClientConfig();
	}
	
	private static void registerClientConfig() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MAIN_CLIENT_SPEC);
	}

}
