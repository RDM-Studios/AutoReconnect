package com.rdm.autoreconnect.reconnecthandlers;

import javax.annotation.Nullable;

import com.rdm.autoreconnect.api.BlacklistedServer;
import com.rdm.autoreconnect.api.IReconnectHandler;
import com.rdm.autoreconnect.manager.ARConfigManager;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.multiplayer.ServerData;

public abstract class BaseReconnectHandler implements IReconnectHandler {
	private final ServerData mainServerData;
	@Nullable
	private final ServerData backupServerData;
	private int reconnectAttempts = -1;
	private int fallbackReconnectAttempts = -1;
	
	public BaseReconnectHandler(ServerData mainServerData, @Nullable ServerData backupServerData) {
		this.mainServerData = mainServerData;
		this.backupServerData = backupServerData;
	}
	
	@Override
	public ServerData getServerData() {
		return mainServerData;
	}
	
	@Override
	public ServerData getBackupServerData() {
		return backupServerData;
	}
	
	public final int getReconnectAttempts() {
		return reconnectAttempts;
	}
	
	public final int getFallbackReconnectAttempts() {
		return fallbackReconnectAttempts;
	}
	
	public final boolean isAttemptingAutoReconnect() {
		return reconnectAttempts >= 0;
	}
	
	public final boolean isAttemptingFallbackAutoReconnect() {
		return fallbackReconnectAttempts >= 0;
	}
	
	public final void continueAttemptingAutoReconnect() {
		reconnectAttempts++;
	}
	
	public final void continueAttemptingFallbackAutoReconnect() {
		fallbackReconnectAttempts++;
	}
	
	@Override
	public boolean shouldReconnect() {
		return !shouldFallbackReconnect() && !isAttemptingFallbackAutoReconnect() && reconnectAttempts <= ARConfigManager.MAIN_CLIENT.maxReconnectAttempts.get();
	}
	
	@Override
	public boolean shouldFallbackReconnect() {
		return !shouldReconnect() && !isAttemptingAutoReconnect() && fallbackReconnectAttempts <= ARConfigManager.MAIN_CLIENT.maxFallbackReconnectAttempts.get();
	}
	
	@Override
	public void tryReconnect() {
		
	}
	
	@Override
	public ObjectArrayList<BlacklistedServer> getBlacklistedServers() {
		return ObjectArrayList.wrap(BlacklistedServer.values());
	}
}
