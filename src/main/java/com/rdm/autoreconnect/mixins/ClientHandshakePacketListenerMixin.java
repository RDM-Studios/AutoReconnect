package com.rdm.autoreconnect.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rdm.autoreconnect.api.AutoReconnectScheduler;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientHandshakePacketListenerMixin {

	@Inject(method = "Lnet/minecraft/client/multiplayer/ClientHandshakePacketListenerImpl;handleHello(Lnet/minecraft/network/protocol/login/ClientboundHelloPacket;)V", at = @At("TAIL"))
	private void ar$handleHello(ClientboundHelloPacket packet, CallbackInfo info) {
		AutoReconnectScheduler scheduler = AutoReconnectScheduler.getInstance();

		if (scheduler.getMpConnectionHandler() != null) {
			if (!scheduler.getMpConnectionHandler().isAttemptingAutoReconnect() && !scheduler.getMpConnectionHandler().isAttemptingFallbackAutoReconnect()) return;
			scheduler.getMpConnectionHandler().resetAutoReconnectAttempts();
			scheduler.getMpConnectionHandler().resetAutoReconnectFallbackAttempts();
		}
	}
}
