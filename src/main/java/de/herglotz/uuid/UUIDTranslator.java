package de.herglotz.uuid;

import java.io.File;
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

	public SearchResult searchForId(String searchString) {
		if (isPossibleUUID(searchString)) {
			return elementCache.findElementContaining(searchString);
		} else {
			return null;
		}
	}

	private boolean isPossibleUUID(String searchString) {
		boolean isId = searchString.length() >= 4; // at least 4 digits
		isId &= searchString.matches("[\\-0-9a-fA-F]+"); // only hex digits or minus sign
		return isId;
	}

}
