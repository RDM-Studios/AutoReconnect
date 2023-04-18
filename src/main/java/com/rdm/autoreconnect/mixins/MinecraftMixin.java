package com.rdm.autoreconnect.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rdm.autoreconnect.api.AutoReconnectScheduler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow
	private Screen screen;
	
	@Inject(method = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;"))
    private void setScreen(Screen newScreen, CallbackInfo info) {
		if (AutoReconnectScheduler.getInstance() != null && screen != null) AutoReconnectScheduler.getInstance().checkScreenChanged(screen, newScreen);
    }
}
