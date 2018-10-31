package de.herglotz.uuid.jni;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {

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
		} catch (NativeHookException e) {
			System.err.println("Registering native keylistener failed. Exiting...");
			System.exit(1);
		}
	}

	private void adaptGlobalScreenLogging() {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);
	}

	public void registerListener(KeyEvent key, Runnable runnable) {
		listeners.put(key, runnable);
	}

	public void unregisterListener(KeyEvent key) {
		listeners.remove(key);
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
		boolean ctrlPressed = (nativeEvent.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0;
		boolean shiftPressed = (nativeEvent.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0;
		KeyEvent key = new KeyEvent(ctrlPressed, shiftPressed, NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
		if (listeners.containsKey(key)) {
			listeners.get(key).run();
		}
	}

}