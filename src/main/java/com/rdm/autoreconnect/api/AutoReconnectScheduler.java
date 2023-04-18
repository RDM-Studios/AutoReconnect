package com.rdm.autoreconnect.api;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import com.rdm.autoreconnect.reconnecthandlers.MultiplayerReconnectHandler;
import com.rdm.autoreconnect.reconnecthandlers.SingleplayerReconnectHandler;
import com.rdm.autoreconnect.util.AutoReconnectUtil;

import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * 
 *
 */
public class AutoReconnectScheduler {
	private static AutoReconnectScheduler INSTANCE;
	private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1);
	private final AtomicReference<ScheduledFuture<?>> timer = new AtomicReference<ScheduledFuture<?>>();
	private IReconnectHandler connectionHandler;
	
	static {
		EXECUTOR.setRemoveOnCancelPolicy(true); // Remove finished/cancelled tasks from queue immediately
	}
	
	public AutoReconnectScheduler() {
		INSTANCE = this;
	}
	
	public static AutoReconnectScheduler getInstance() {
		return INSTANCE;
	}
	
	public static void setInstance(AutoReconnectScheduler schedulerInstance) {
		INSTANCE = schedulerInstance;
	}
	
	/**
	 * Schedule the specified task to be executed after a set number of seconds.
	 * @param task The task to execute.
	 * @param delayInSeconds The delay (in seconds) before the task is executed.
	 */
	public static ScheduledFuture<?> scheduleTask(Runnable task, long delayInSeconds) {
		return scheduleTask(task, delayInSeconds, TimeUnit.SECONDS);
	}
	
	/**
	 * Schedule the specified task to be executed after a set number of whatever TimeUnit is set.
	 * @param task The task to execute.
	 * @param delay The delay (in specified TimeUnit) before the task is executed.
	 * @param unit The time unit by which to schedule the deferred task.
	 */
	public static ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit unit) {
		return EXECUTOR.schedule(task, delay, unit);
	}
	
	/**
	 * Safely set the current connection handler being handled by the scheduler to another instance.
	 * @param handler The new handler the current connection handler should be set to.
	 */
	public void setConnectionHandler(MultiplayerReconnectHandler handler) {
		if (connectionHandler != null && connectionHandler.getClass().equals(handler.getClass()) && connectionHandler.getServerData().name.equals(handler.getServerData().name)) return;
		this.connectionHandler = handler;
	}
	
	/**
	 * Begin a countdown/timer (in seconds), which utilizes the {@link countdown(Integer, IntConsumer)} method.
	 * @param consCb An IntConsumer callback.
	 * @param isFallback Whether or not the countdown should begin for the fallback server or main target server.
	 */
	public final void beginCountdown(final IntConsumer consCb, boolean isFallback) {
		try {
			if (timer.get() == null) {
				if (isFallback) connectionHandler.continueAttemptingFallbackAutoReconnect();
				else connectionHandler.continueAttemptingAutoReconnect();
				countdown(isFallback ? connectionHandler.getFallbackAutoReconnectInterval() : connectionHandler.getAutoReconnectInterval(), consCb);
			}
		} catch (IndexOutOfBoundsException e) {
			consCb.accept(-1);
		}
	}
	
	/**
	 * A timer-decrementing method which utilizes recursion in order to simulate a real timer.
	 * @param durSeconds The duration of the countdown (in seconds).
	 * @param consCb An IntConsumer callback.
	 */
	private final void countdown(Integer durSeconds, final IntConsumer consCb) {
		if (durSeconds == 0) { // Make sure that it's not less than 0, otherwise it could cause some logical errors with how auto-reconnect functions
			Minecraft.getInstance().execute(connectionHandler::tryReconnect);
			return;
		}
		
		consCb.accept((int) durSeconds);
		
		synchronized (timer) {
			timer.set(scheduleTask(() -> countdown(durSeconds - 1, consCb), 1));
		}
	}
	
	/**
	 * A method used to cancel/reset all auto-reconnect attempts, including ones to the fallback server (if it exists).
	 */
	public final void cancelAllReconnectAttempts() {
		if (connectionHandler != null) {
			connectionHandler.resetAutoReconnectAttempts();
			connectionHandler.resetAutoReconnectFallbackAttempts();
			
			synchronized (timer) {
				if (timer.get() != null) timer.getAndSet(null).cancel(true);
			}
		}
	}
	
	/**
	 * Shortcut method (w/A nullity check, for safety) to {@link IReconnectHandler#tryReconnect()}.
	 */
	public void tryReconnect() {
		if (connectionHandler != null) connectionHandler.tryReconnect();
	}
	
	/**
	 * Getter for the multiplayer connection handler in this class.
	 * @return This class's multiplayer connection handler.
	 */
	@Nullable
	public MultiplayerReconnectHandler getMpConnectionHandler() {
		if (!(connectionHandler instanceof MultiplayerReconnectHandler)) return null;
		return (MultiplayerReconnectHandler) connectionHandler;
	}
	
	public boolean isSingleplayer() {
		return connectionHandler instanceof SingleplayerReconnectHandler;
	}
	
	/**
	 * Checker method to validate that a screen has changed to another.
	 * @param from Screen A.
	 * @param to Screen B.
	 */
	public void checkScreenChanged(Screen from, Screen to) {
		if (from == null && to == null) return;
		if (from.getClass().equals(to.getClass())) return;
		
		if ((AutoReconnectUtil.isVanillaMainScreen(from) && AutoReconnectUtil.isVanillaMainScreen(to)) || AutoReconnectUtil.isReauthenticating(from, to))
			connectionHandler = null;
	}
}
