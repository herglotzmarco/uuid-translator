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
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	private ExecutorService executorService;

	public TrayApplication() {
		dialog = new AlertDialog();
		translator = new UUIDTranslator();
		executorService = Executors.newFixedThreadPool(2);
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
			LOG.info("Tray Application started");
		} catch (AWTException e) {
			LOG.error("Error adding TrayIcon to SystemTray. Exiting...");
			System.exit(1);
		}
	}

	private BufferedImage getImageForTrayIcon() {
		try {
			return ImageIO.read(this.getClass().getClassLoader().getResource("icons/trayicon.png"));
		} catch (IOException e) {
			LOG.warn("Icon for Tray Application could not be found", e);
			return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		}
	}

	private PopupMenu buildPopupMenu() {
		PopupMenu menu = new PopupMenu();
		menu.add(new SelectWorkspaceIcon(this::updateElements));
		menu.addSeparator();
		menu.add(new ExitIcon(trayIcon));
		return menu;
	}

	private void updateElements(Set<File> files) {
		executorService.execute(() -> translator.updateElements(files));
	}

	private void registerListener() {
		GlobalKeyListener listener = GlobalKeyListener.instance();
		listener.registerListener(new KeyEvent(true, false, "C"), this::searchForUUID);
	}

	private void searchForUUID() {
		String searchString = getClipboardContent().trim();
		executorService.execute(() -> searchForUUID(searchString));
	}

	private void searchForUUID(String searchString) {
		SearchResult result = translator.searchForId(searchString);
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
			LOG.warn("Reading ClipboardContent failed", e);
			return "";
		}
	}

}
