package de.herglotz.uuid.ui;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.uuid.SearchResult;
import de.herglotz.uuid.UUIDTranslator;
import de.herglotz.uuid.jni.GlobalKeyListener;
import de.herglotz.uuid.jni.KeyEvent;

public class TrayApplication {

	private static final Logger LOG = LoggerFactory.getLogger(TrayApplication.class);

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
			LOG.error("SystemTray is no supported. Exiting...");
			System.exit(1);
		}

		try {
			trayIcon = new TrayIcon(getImageForTrayIcon(), "UUID Translator", buildPopupMenu());
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			LOG.error("Error adding TrayIcon to SystemTray. Exiting...");
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
		SearchResult result = translator.searchForId(getClipboardContent().trim());
		if (result == null) {
			return;
		}
		if (result.hasError()) {
			// TODO: Better error reporting
			dialog.showPopup(result.getErrorMessage());
		} else {
			dialog.showPopup(result.getName());
		}
	}

	private String getClipboardContent() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			return clipboard.getData(DataFlavor.stringFlavor).toString();
		} catch (UnsupportedFlavorException | IOException e) {
			return "";
		}
	}

}
