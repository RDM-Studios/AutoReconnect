package com.rdm.autoreconnect.api;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.multiplayer.ServerData;

public interface IReconnectHandler {
	
	/**
	 * The main method which attempts to reconnect to the target/backup server.
	 */
	void tryReconnect();
	
	/**
	 * Specifies whether or not the mod should attempt to reconnect the client to their target server.
	 * @return true to allow the client to reconnect to their target server, else returns false.
	 */
	boolean shouldReconnect();
	
	/**
	 * Specifies whether or not the mod should attempt to reconnect the client to the specified fallback server.
	 * @return tru to allow the client to reconnect to their target server, else returns false.
	 */
	boolean shouldFallbackReconnect();
	
	/**
	 * A list of servers which will be blacklisted from autoreconnect functionality (e.g. Hypixel), in order to avoid liability
	 * for unauthorized client action.
	 * @return A list of servers blacklisted from autoreconnect.
	 */
	ObjectArrayList<BlacklistedServer> getBlacklistedServers();
	
	/**
	 * The main target server data to reconnect to.
	 * @return The main target server data to reconnect to by default.
	 */
	ServerData getServerData();
	
	/**
	 * The backup target server data, in case all validated attempts to reconnect to the main target server fail.
	 * @return The backup target server data to reconnect to.
	 */
	@Nullable
	ServerData getBackupServerData();
	
}
