package de.herglotz.uuid.application;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.uuid.elements.ElementRegistry;
import de.herglotz.uuid.jni.GlobalKeyListener;
import de.herglotz.uuid.search.SearchResult;
import de.herglotz.uuid.search.SearchResultType;
import de.herglotz.uuid.search.UUIDSearcher;
import de.herglotz.uuid.settings.Settings;
import de.herglotz.uuid.ui.AlertDialog;

class TrayApplication {

	private static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

	private static final Logger LOG = LoggerFactory.getLogger(TrayApplication.class);

	private AlertDialog ui;
	private ElementRegistry elementContainer;
	private ExecutorService executorService;
	private Settings settings;

	public TrayApplication() {
		ui = new AlertDialog();
		elementContainer = new ElementRegistry();
		executorService = Executors.newFixedThreadPool(2);
		settings = new Settings();
		registerTrayIcon();
		registerKeyListener();
	}

	private void registerTrayIcon() {
		if (!SystemTray.isSupported()) {
			LOG.error("SystemTray is no supported. Exiting...");
			System.exit(1);
		}

		try {
			TrayIcon trayIcon = new TrayIcon(getImageForTrayIcon(), "UUID Translator");
			trayIcon.setPopupMenu(buildPopupMenu(trayIcon));
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

	private PopupMenu buildPopupMenu(TrayIcon trayIcon) {
		PopupMenu menu = new PopupMenu();
		menu.add(new SelectWorkspaceIcon(this::updateElements));
		menu.add(new SettingsIcon(settings, this::registerKeyListener));
		menu.addSeparator();
		menu.add(new ExitIcon(trayIcon));
		return menu;
	}

	private void updateElements(Set<File> files) {
		executorService.execute(() -> {
			ui.showMessage("Updating registry...");
			elementContainer.updateElements(files);
			ui.showMessage("Update done.");
		});
	}

	private void registerKeyListener() {
		GlobalKeyListener listener = GlobalKeyListener.instance();
		listener.registerListener(settings.getSearchHotkey(), this::searchForUUID);
		listener.registerListener(settings.getReplaceHotkey(), this::replaceAllUUIDs);
	}

	private void searchForUUID() {
		String searchString = getClipboardContent().trim();
		executorService.execute(() -> searchForUUID(searchString));
	}

	private void searchForUUID(String searchString) {
		UUIDSearcher searcher = new UUIDSearcher(elementContainer);
		SearchResult result = searcher.searchForUUID(searchString);
		processSearchResult(result);
	}

	private void processSearchResult(SearchResult result) {
		switch (result.getType()) {
		case INVALID:
			return; // no search was needed
		case EMPTY:
		case MULTIPLE:
			ui.showError(result.getMessage());
			break;
		case ONE:
			String message = result.getElement().getName();
			if (settings.isShowType()) {
				message = result.getElement().getType() + " " + result.getElement().getName();
			}
			ui.showMessage(message);
			break;
		default:
			throw new IllegalArgumentException("Unknown SearchResult type: " + result.getType());
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

	private void replaceAllUUIDs() {
		String clipboard = getClipboardContent().trim();
		executorService.execute(() -> replaceAllUUIDs(clipboard));
	}

	private void replaceAllUUIDs(String clipboard) {
		LOG.debug("Replacing all UUIDs");
		String result = doReplacement(clipboard);
		setClipboardContent(result);
		LOG.debug("Replacement complete.");
		ui.showMessage("Replacement complete.");
	}

	private String doReplacement(String clipboard) {
		Matcher matcher = Pattern.compile(UUID_REGEX).matcher(clipboard);
		String result = new String(clipboard);
		UUIDSearcher searcher = new UUIDSearcher(elementContainer);
		while (matcher.find()) {
			String id = clipboard.substring(matcher.start(), matcher.end());
			SearchResult search = searcher.searchForUUID(id);
			if (search.getType() == SearchResultType.ONE) {
				result = result.replace(id, search.getMessage());
			}
		}
		return result;
	}

	private void setClipboardContent(String newContent) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(newContent), null);
	}

}
