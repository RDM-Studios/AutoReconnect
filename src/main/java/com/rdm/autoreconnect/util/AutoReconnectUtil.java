package com.rdm.autoreconnect.util;

import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;

import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

public class AutoReconnectUtil {
	
	public static boolean isReauthenticating(Screen from, Screen to) {
		if (from == null || to == null) return false;
		return from instanceof DisconnectedScreen && to.getClass().getSimpleName().equals("AuthScreen");
	}
	
	public static boolean isVanillaMainScreen(Screen screen) {
		return screen instanceof TitleScreen || screen instanceof SelectWorldScreen || screen instanceof JoinMultiplayerScreen || screen instanceof RealmsCreateRealmScreen;
	}
}
