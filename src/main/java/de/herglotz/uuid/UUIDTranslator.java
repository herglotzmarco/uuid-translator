package de.herglotz.uuid;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UUIDTranslator {

	private static final Logger LOG = LoggerFactory.getLogger(UUIDTranslator.class);

	private final ElementCache elementCache;

	public UUIDTranslator() {
		elementCache = new ElementCache();
	}

	public void updateElements(Set<File> files) {
		LOG.debug("Updating registry for new workspace");
		files.stream()//
				.map(ElementParser::readAndParse)//
				.flatMap(Collection::stream)//
				.forEach(p -> elementCache.put(p.getKey(), p.getValue()));
	}

	public SearchResult searchForId(String searchString) {
		LOG.debug("Searching for String [{}]", searchString);
		if (isPossibleUUID(searchString)) {
			LOG.debug("String [{}] was a valid UUID. Searching registry", searchString);
			return elementCache.findElementContaining(searchString);
		} else {
			LOG.debug("String [{}] was not a valid UUID. Search cancelled", searchString);
			return null;
		}
	}

	private boolean isPossibleUUID(String searchString) {
		boolean isId = searchString.length() >= 4; // at least 4 digits
		isId &= searchString.matches("[\\-0-9a-fA-F]+"); // only hex digits or minus sign
		return isId;
	}

}
