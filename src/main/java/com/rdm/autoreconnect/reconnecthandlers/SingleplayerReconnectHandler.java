package com.rdm.autoreconnect.reconnecthandlers;

import com.rdm.autoreconnect.api.BlacklistedServer;
import com.rdm.autoreconnect.api.IReconnectHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class SingleplayerReconnectHandler implements IReconnectHandler {
	private String worldName;
	
	public SingleplayerReconnectHandler(String worldName) {
		this.worldName = worldName;
	}

	@Override
	public void tryReconnect() {
		Minecraft mc = Minecraft.getInstance();
		
		if (mc.getLevelSource().levelExists(worldName)) {
	//        mc.forceSetScreen(new net.minecraft.client.gui.screens.LevelLoadingScreen(Component.translatable("selectWorld.data_read")));
	//        mc.getLevelSource().createAccess(worldName);
		}
	}

	@Override
	public boolean shouldReconnect() {
		return false;
	}

	@Override
	public boolean shouldFallbackReconnect() {
		return false;
	}

	@Override
	public ObjectArrayList<BlacklistedServer> getBlacklistedServers() {
		return null;
	}

	@Override
	public ServerData getServerData() {
		return null;
	}

	@Override
	public ServerData getBackupServerData() {
		return null;
	}

	@Override
	public void resetAutoReconnectAttempts() {		
	}

	@Override
	public void resetAutoReconnectFallbackAttempts() {		
	}

	@Override
	public void continueAttemptingAutoReconnect() {		
	}

	@Override
	public void continueAttemptingFallbackAutoReconnect() {		
	}

}
