package com.rdm.autoreconnect.api;

public enum BlacklistedServer {
	HYPIXEL("Hypixel", "mc.hypixel.net", "Hypixel is a large community server featuring games such as skyblock, which may require a lot of AFKing. As such, since auto-reconnecting is unauthorized on that server, it is blacklisted from the functionality of Auto Reconnect."),
	HIVE("The Hive", "play.hivemc.com", "The Hive is a server with several PVP minigames that are regularly hacked on. Since auto-reconnecting is unauthorized on that server, it has been blacklisted from Auto Reconnect."),
	MINEPLEX("MinePlex", "mineplex.com", "Mineplex is a server which is regularly hacked on. As such, hackers take advantage of the relatively poor protection from auto-reconnecting the server has in order to abuse their hacks. Since auto-reconnecting is unauthorized on that server, it has been blacklisted from Auto Reconnect.");
	
	private final String serverName;
	private final String serverIp;
	private final String reason;
	
	BlacklistedServer(String serverName, String serverIp, String reason) {
		this.serverName = serverName;
		this.serverIp = serverIp;
		this.reason = reason;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public String getServerIp() {
		return serverIp;
	}
	
	public String getBlacklistReason() {
		return reason;
	}
	
	public static BlacklistedServer getBlacklistedServerByName(String serverName) {
		BlacklistedServer target = null;
		
		for (BlacklistedServer tServer : values()) {
			if (tServer.getServerName().equals(serverName)) {
				target = tServer;
				break;
			}
		}
		
		return target;
	}
	
	public static BlacklistedServer getBlacklistedServerByIp(String serverIp) {
		BlacklistedServer target = null;
		
		for (BlacklistedServer tServer : values()) {
			if (tServer.getServerIp().equals(serverIp)) {
				target = tServer;
				break;
			}
		}
		
		return target;
	}

}
