package com.rdm.autoreconnect.reconnecthandlers;

import javax.annotation.Nullable;

import com.rdm.autoreconnect.api.BlacklistedServer;
import com.rdm.autoreconnect.api.IReconnectHandler;
import com.rdm.autoreconnect.manager.ARConfigManager;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class MultiplayerReconnectHandler implements IReconnectHandler {
	private final ServerData mainServerData;
	@Nullable
	private final ServerData backupServerData;
	private int reconnectAttempts = -1;
	private int fallbackReconnectAttempts = -1;
	private boolean hasAttemptedFallbackAutoReconnect = false;
	
	public MultiplayerReconnectHandler(ServerData mainServerData, @Nullable ServerData backupServerData) {
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
		if (reconnectAttempts < ARConfigManager.MAIN_CLIENT.maxReconnectAttempts.get()) reconnectAttempts++;
	}
	
	public final void continueAttemptingFallbackAutoReconnect() {
		if (fallbackReconnectAttempts < ARConfigManager.MAIN_CLIENT.maxFallbackReconnectAttempts.get()) fallbackReconnectAttempts++;
	}
	
	public final void resetAutoReconnectAttempts() {
		this.reconnectAttempts = -1;
	}
	
	public final void resetAutoReconnectFallbackAttempts() {
		this.hasAttemptedFallbackAutoReconnect = false;
		this.fallbackReconnectAttempts = -1;
	}
	
	public final boolean hasAttemptedFallbackAutoReconnect() {
		return hasAttemptedFallbackAutoReconnect;
	}
	
	public boolean isTargetServerBlacklisted() {
		for (BlacklistedServer invServer : BlacklistedServer.values()) {
			if (invServer.getServerIp().equals(getServerData().ip)) return true;
		}
		
		return false;
	}
	
	public boolean isFallbackServerBlacklisted() {
		if (getBackupServerData() == null) return false;
		
		for (BlacklistedServer invServer : BlacklistedServer.values()) {
			if (invServer.getServerIp().equals(getBackupServerData().ip)) return true;
		}
		
		return false;
	}
	
	@Override
	public boolean shouldReconnect() {
		return !isTargetServerBlacklisted() && !isAttemptingFallbackAutoReconnect() && reconnectAttempts <= ARConfigManager.MAIN_CLIENT.maxReconnectAttempts.get();
	}
	
	@Override
	public boolean shouldFallbackReconnect() {
		if (getBackupServerData() == null) return false;
		
		return !isFallbackServerBlacklisted() && !shouldReconnect() && !isAttemptingAutoReconnect() && fallbackReconnectAttempts <= ARConfigManager.MAIN_CLIENT.maxFallbackReconnectAttempts.get() && reconnectAttempts < 0;
	}
	
	@Override
	public void tryReconnect() {
		TitleScreen titleScreen = new TitleScreen();
		JoinMultiplayerScreen mpScreen = new JoinMultiplayerScreen(titleScreen);
		Minecraft mc = Minecraft.getInstance();
		
		if (shouldReconnect()) {		
			ConnectScreen.startConnecting(mpScreen, mc, ServerAddress.parseString(mainServerData.ip), mainServerData);
		} 
		
		if (!shouldReconnect()) resetAutoReconnectAttempts();
		
		if (shouldFallbackReconnect()) {
			ConnectScreen.startConnecting(mpScreen, mc, ServerAddress.parseString(backupServerData.ip), backupServerData);
			hasAttemptedFallbackAutoReconnect = true;
		}
	}
	
	@Override
	public ObjectArrayList<BlacklistedServer> getBlacklistedServers() {
		return ObjectArrayList.wrap(BlacklistedServer.values());
	}
}
