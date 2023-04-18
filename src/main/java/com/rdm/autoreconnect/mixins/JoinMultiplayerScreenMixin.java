package com.rdm.autoreconnect.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rdm.autoreconnect.api.AutoReconnectScheduler;
import com.rdm.autoreconnect.manager.ARConfigManager;
import com.rdm.autoreconnect.reconnecthandlers.MultiplayerReconnectHandler;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin extends Screen {

	public JoinMultiplayerScreenMixin(Component pTitle) {
		super(pTitle);
	}

	@Inject(method = "Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;join(Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("TAIL"))
	private void ar$join(ServerData targetServerData, CallbackInfo info) {
		if (AutoReconnectScheduler.getInstance() != null) AutoReconnectScheduler.getInstance().setConnectionHandler(new MultiplayerReconnectHandler(targetServerData, new ServerData(ARConfigManager.MAIN_CLIENT.fallbackServerName.get(), ARConfigManager.MAIN_CLIENT.fallbackServerIP.get(), ARConfigManager.MAIN_CLIENT.isFallbackServerLan.get())));
	}
}
