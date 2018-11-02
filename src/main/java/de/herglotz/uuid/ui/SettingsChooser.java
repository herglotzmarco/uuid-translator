package de.herglotz.uuid.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import de.herglotz.uuid.jni.GlobalKeyListener;
import de.herglotz.uuid.jni.KeyEvent;

public class SettingsChooser extends JButton implements ActionListener, NativeKeyListener {

	private static final long serialVersionUID = 2816649445590739088L;

	private Consumer<KeyEvent> setter;
	private Runnable callback;

	public SettingsChooser(Supplier<KeyEvent> getter, Consumer<KeyEvent> setter, Runnable callback) {
		this.setter = setter;
		this.callback = callback;
		setText(getter.get().toString());
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setText("choose...");
		GlobalScreen.addNativeKeyListener(this);
		GlobalKeyListener.instance().unregisterAll();
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
		GlobalScreen.removeNativeKeyListener(this);
		setText(key.toString());
		setter.accept(key);
		SwingUtilities.invokeLater(callback); // hack to delay the callback to avoid instantly calling the new hotkey
	}

}
