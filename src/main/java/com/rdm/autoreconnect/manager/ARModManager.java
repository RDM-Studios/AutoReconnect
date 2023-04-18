package com.rdm.autoreconnect.manager;

import com.rdm.autoreconnect.AutoReconnect;
import com.rdm.autoreconnect.api.AutoReconnectScheduler;

import net.minecraftforge.common.MinecraftForge;

public class ARModManager {
	
	public static void registerAll() {
		ARConfigManager.registerConfigs();
		
		if (AutoReconnectScheduler.getInstance() == null) AutoReconnectScheduler.setInstance(new AutoReconnectScheduler());
		
		MinecraftForge.EVENT_BUS.register(AutoReconnect.INSTANCE);
	}

}
