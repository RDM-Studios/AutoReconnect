package com.rdm.autoreconnect;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.rdm.autoreconnect.manager.ARModManager;

import net.minecraftforge.fml.common.Mod;

@Mod(AutoReconnect.MODID)
public class AutoReconnect {
    public static final String MODID = "autoreconnect";
    public static String MODNAME = "Auto Reconnect";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static AutoReconnect INSTANCE;

    public AutoReconnect() {
    	INSTANCE = this;
    	
    	ARModManager.registerAll();
    }
    
    
}
