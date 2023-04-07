package com.rdm.autoreconnect.config;

import java.util.List;

import com.rdm.autoreconnect.util.ValidationUtil;

import it.unimi.dsi.fastutil.chars.CharArrayList;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ARConfig {
	private final CharArrayList serverIPList = new CharArrayList();
	
	public final ConfigValue<List<? extends Character>> serverIPs;
	
	public ARConfig(Builder configBuilder) {
		configBuilder.push("Servers");
		serverIPs = configBuilder
				.comment("A list of servers which the mod will autoreconnect to. Blacklisted servers NOT included.")
				.defineList("Auto Reconnect Server IPs", serverIPList, (entry) -> entry instanceof String && ValidationUtil.isValidServerIp((String) entry));
		configBuilder.pop();
	}

}
