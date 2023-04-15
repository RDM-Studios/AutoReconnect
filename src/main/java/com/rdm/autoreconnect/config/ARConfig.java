package com.rdm.autoreconnect.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ARConfig {	
	public final ConfigValue<String> targetServerIP;
	public final ConfigValue<String> fallbackServerIP;
	
	public final IntValue maxReconnectAttempts;
	public final IntValue maxFallbackReconnectAttempts;
	
	public final IntValue autoReconnectInterval;
	public final IntValue fallbackAutoReconnectInterval;
	
	public ARConfig(Builder configBuilder) {
		configBuilder.push("Servers");
		targetServerIP = configBuilder
				.comment("The server to auto-reconnect to. Is localhost by default.")
				.define("Target Server IP", "localhost");
		fallbackServerIP = configBuilder
				.comment("The fallback server to auto-reconnect to, in case of failure in auto-reconnecting to the original target server. Is localhost by default.")
				.define("Fallback Server IP", "localhost");
		maxReconnectAttempts = configBuilder
				.comment("The maximum amount of times Auto Reconnect will attempt to reconnect to the main target server.")
				.defineInRange("Max Reconnect Attempts", 10, 1, 100);
		maxFallbackReconnectAttempts = configBuilder
				.comment("The maximum amount of times Auto Reconnect will attempt to reconnect to the fallback server.")
				.defineInRange("Max Fallback Reconnect Attempts", 10, 1, 100);
		autoReconnectInterval = configBuilder
				.comment("The time interval (in seconds) between every auto-reconnect attempt to the main target server.")
				.defineInRange("Auto Reconnect Interval", 2, 1, 60);
		fallbackAutoReconnectInterval = configBuilder
				.comment("The time interval (in seconds) between every auto-reconnect attempt to the fallback server.")
				.defineInRange("Fallback Auto Reconnect Interval", 2, 1, 60);
		configBuilder.pop();
		
		configBuilder.push("Misc");
		
		configBuilder.pop();
	}

}
