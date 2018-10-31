package de.herglotz.uuid.ui;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.herglotz.uuid.UUIDTranslator;
import de.herglotz.uuid.jni.GlobalKeyListener;
import de.herglotz.uuid.jni.KeyEvent;

public class TrayApplication {

	private TrayIcon trayIcon;
	private AlertDialog dialog;
	private UUIDTranslator translator;

	public TrayApplication() {
		dialog = new AlertDialog();
		translator = new UUIDTranslator();
		createFrame();
		registerListener();
	}

	private void createFrame() {
		if (!SystemTray.isSupported()) {
			System.err.println("SystemTray is no supported. Exiting...");
			System.exit(1);
		}

		try {
			trayIcon = new TrayIcon(getImageForTrayIcon(), "UUID Translator", buildPopupMenu());
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			System.err.println("Error adding TrayIcon to SystemTray. Exiting...");
			System.exit(1);
		}
	}

	private BufferedImage getImageForTrayIcon() {
		try {
			return ImageIO.read(this.getClass().getClassLoader().getResource("icons/trayicon.png"));
		} catch (IOException e) {
			return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		}
	}

	private PopupMenu buildPopupMenu() {
		PopupMenu menu = new PopupMenu();
		menu.add(new SelectWorkspaceIcon(translator::updateElements));
		menu.addSeparator();
		menu.add(new ExitIcon(trayIcon));
		return menu;
	}

	private void registerListener() {
		GlobalKeyListener listener = GlobalKeyListener.instance();
		listener.registerListener(new KeyEvent(true, false, "C"), this::searchForId);
	}

	private void searchForId() {
		dialog.showPopup(translator.searchForId());
	}

}
