package de.herglotz.uuid.jni;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalKeyListener implements NativeKeyListener {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalKeyListener.class);

	private static GlobalKeyListener instance;

	private Map<KeyEvent, Runnable> listeners;

	public static GlobalKeyListener instance() {
		if (instance == null) {
			instance = new GlobalKeyListener();
		}
		return instance;
	}

	private GlobalKeyListener() {
		listeners = new HashMap<>();
		try {
			GlobalScreen.registerNativeHook();
			adaptGlobalScreenLogging();
			GlobalScreen.addNativeKeyListener(this);
			LOG.info("Global KeyListener registered");
		} catch (NativeHookException e) {
			LOG.error("Registering native keylistener failed. Exiting...");
			System.exit(1);
		}
	}

	private void adaptGlobalScreenLogging() {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);
	}

	public void registerListener(KeyEvent key, Runnable runnable) {
		listeners.put(key, runnable);
		LOG.info("KeyListener registered for {}", key);
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
		KeyEvent key = KeyEvent.fromNativeKeyEvent(nativeEvent);
		LOG.trace("Incoming Key: {}", key);
		if (listeners.containsKey(key)) {
			LOG.debug("Calling listener for {}", key);
			listeners.get(key).run();
		}
	}

	public void unregisterAll() {
		listeners.clear();
		LOG.info("Unregistered all KeyListeners");
	}

}
