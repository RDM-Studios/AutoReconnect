package com.rdm.autoreconnect.manager;

import com.rdm.autoreconnect.AutoReconnect;

import net.minecraftforge.common.MinecraftForge;

public class ARModManager {
	
	public static void registerAll() {
		ARConfigManager.registerConfigs();
		
		MinecraftForge.EVENT_BUS.register(AutoReconnect.INSTANCE);
	}

}
