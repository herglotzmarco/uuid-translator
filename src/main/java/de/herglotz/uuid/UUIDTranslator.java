package de.herglotz.uuid;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class UUIDTranslator {

	private final ElementCache elementCache;

	public UUIDTranslator() {
		elementCache = new ElementCache();
	}

	public void updateElements(Set<File> files) {
		files.stream()//
				.map(ElementParser::readAndParse)//
				.flatMap(Collection::stream)//
				.forEach(p -> elementCache.put(p.getKey(), p.getValue()));
	}

	public SearchResult searchForId() {
		// TODO: Check if it is an id, otherwise we don't need to do anything
		return elementCache.findElementContaining(getClipboardContent());
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
