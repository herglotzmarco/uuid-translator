package de.herglotz.uuid;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UUIDTranslator {

	private final ElementCache elementCache;

	public UUIDTranslator() {
		elementCache = new ElementCache();
	}

	public void updateElements(Set<File> files) {
		// TODO: implement real update
		elementCache.clear();
		elementCache.put(UUID.randomUUID().toString(), "Blubb");
		elementCache.put(UUID.randomUUID().toString(), "Bla");
		elementCache.put(UUID.randomUUID().toString(), "Fasel");
	}

	public String searchForId() {
		String content = getClipboardContent();
		Optional<String> name = elementCache.get(content);
		return name.orElse("Unknown Id");
	}

	private String getClipboardContent() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			return clipboard.getData(DataFlavor.stringFlavor).toString();
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
