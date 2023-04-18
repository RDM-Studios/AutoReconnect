package com.rdm.autoreconnect.mixins;

import java.util.concurrent.TimeUnit;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rdm.autoreconnect.api.AutoReconnectScheduler;
import com.rdm.autoreconnect.api.BlacklistedServer;
import com.rdm.autoreconnect.api.References;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {
	@Shadow
	@Final
	@Mutable
	private Component reason;
	@Shadow
	@Final
	@Mutable
	private Screen parent;
	private Button reconnectButton, cancelButton;
	private boolean shouldReconnect, shouldFallbackReconnect;

	public DisconnectedScreenMixin(Component pTitle) {
		super(pTitle);
	}
	
	@Inject(method = "Lnet/minecraft/client/gui/screens/DisconnectedScreen;<init>(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)V", at = @At("TAIL"))
	private void setupButtons(Screen parent, Component title, Component reason, CallbackInfo info) {		
		if (AutoReconnectScheduler.getInstance().getMpConnectionHandler() != null) {
			this.reconnectButton = Button.builder(Component.literal(References.DISCONNECT_SCREEN_RECONNECT).withStyle(ChatFormatting.GREEN), button -> AutoReconnectScheduler.scheduleTask(() -> Minecraft.getInstance().execute(() -> {
				AutoReconnectScheduler.getInstance().cancelAllReconnectAttempts();
				AutoReconnectScheduler.getInstance().tryReconnect();
			}), 100, TimeUnit.MILLISECONDS)).bounds(0, 0, 0, 20).build();
			this.shouldReconnect = AutoReconnectScheduler.getInstance().getMpConnectionHandler().shouldReconnect();
			this.shouldFallbackReconnect = AutoReconnectScheduler.getInstance().getMpConnectionHandler().shouldFallbackReconnect();
			
			if (shouldReconnect) AutoReconnectScheduler.getInstance().beginCountdown(this::timerCountdownCallback, false);
			else if (shouldFallbackReconnect) AutoReconnectScheduler.getInstance().beginCountdown(this::timerCountdownCallback, true);
		}
	}
	
	@Inject(method = "Lnet/minecraft/client/gui/screens/DisconnectedScreen;shouldCloseOnEsc()Z", at = @At("RETURN"))
	private void ar$shouldCloseOnEsc(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(true);
	}
	
	@Inject(method = "Lnet/minecraft/client/gui/screens/DisconnectedScreen;init()V", at = @At("TAIL"))
	private void ar$init(CallbackInfo info) {
		if (AutoReconnectScheduler.getInstance().getMpConnectionHandler() != null) {
			Button backButton = (Button) children().get(0);
			
			reconnectButton.setX(backButton.getX());
			reconnectButton.setY(backButton.getY());
			
			if (shouldReconnect || shouldFallbackReconnect) {
				reconnectButton.setWidth(backButton.getWidth() - backButton.getHeight() - 4);
				cancelButton = Button.builder(Component.literal(References.DISCONNECT_SCREEN_CANCEL_AUTO_RECONNECT).withStyle(ChatFormatting.RED), button -> cancelTimerCountdown()).bounds(backButton.getX() + backButton.getWidth() - backButton.getHeight(), backButton.getY(), backButton.getHeight(), backButton.getHeight()).build();
				addRenderableWidget(cancelButton);
			} else reconnectButton.setWidth(backButton.getWidth());
			
			addRenderableWidget(reconnectButton);
			backButton.setY(backButton.getY() + backButton.getHeight() + 4);
		}
	}
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (pKeyCode == 256) {
			cancelTimerCountdown();
			return true;
		} else return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	@Override
	public void onClose() {
		if (minecraft != null) minecraft.setScreen(parent);
	}
	
	private void cancelTimerCountdown() {
		if (AutoReconnectScheduler.getInstance().getMpConnectionHandler() != null) {
			AutoReconnectScheduler.getInstance().cancelAllReconnectAttempts();
			shouldReconnect = false;
			shouldFallbackReconnect = false;
			removeWidget(cancelButton);
			reconnectButton.active = true;
			reconnectButton.setMessage(Component.literal(References.DISCONNECT_SCREEN_RECONNECT).withStyle(ChatFormatting.GREEN)); // As per client's request
			reconnectButton.setWidth(((Button) children().get(0)).getWidth());
		}
	}
	
	//TODO Goofy ahh checks
	private void timerCountdownCallback(Integer durSeconds) {
		if (AutoReconnectScheduler.getInstance().getMpConnectionHandler() != null) {
			if (durSeconds < 0) {
				reconnectButton.setMessage(Component.literal(References.DISCONNECT_SCREEN_AUTO_RECONNECT_FAIL).withStyle(ChatFormatting.RED));
				this.reconnectButton.active = false;
			} else if (AutoReconnectScheduler.getInstance().getMpConnectionHandler().isTargetServerBlacklisted()) {
				this.reason = Component.literal(BlacklistedServer.getBlacklistedServerByName(AutoReconnectScheduler.getInstance().getMpConnectionHandler().getServerData().name).getBlacklistReason());
				this.reconnectButton.active = false;
			} else if (AutoReconnectScheduler.getInstance().getMpConnectionHandler().isFallbackServerBlacklisted()) {
				this.reason = Component.literal(BlacklistedServer.getBlacklistedServerByName(AutoReconnectScheduler.getInstance().getMpConnectionHandler().getBackupServerData().name).getBlacklistReason());
				this.reconnectButton.active = false;
			} else {
				reconnectButton.setMessage(Component.literal(References.DISCONNECT_SCREEN_AUTO_RECONNECTING + durSeconds).withStyle(ChatFormatting.GREEN));
			}
		}
	}
}
