package com.rdm.autoreconnect.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rdm.autoreconnect.api.References;

import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin extends Screen {
	@Shadow
	private Component status;
	
	protected ConnectScreenMixin(Component pTitle) {
		super(pTitle);
	}
	
	@Inject(method = "Lnet/minecraft/client/gui/screens/ConnectScreen;<init>(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("TAIL"))
	private void changeStatus(Screen parent, CallbackInfo info) {
		status = Component.literal(References.CONNECT_SCREEN_CONNECTING);
	}
}
