package de.herglotz.uuid.search;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.uuid.elements.ElementRegistry;
import de.herglotz.uuid.elements.RegistryElement;

public class UUIDSearcher {

	private static final Logger LOG = LoggerFactory.getLogger(UUIDSearcher.class);

	private ElementRegistry elementContainer;

	public UUIDSearcher(ElementRegistry elementContainer) {
		this.elementContainer = elementContainer;
	}

	public SearchResult searchForUUID(String searchString) {
		LOG.debug("Searching for String [{}]", searchString);
		if (isPossibleUUID(searchString)) {
			LOG.debug("String [{}] was a valid UUID. Searching registry", searchString);
			Collection<RegistryElement> searchResults = elementContainer.findElementsContaining(searchString);
			return processResults(searchResults, searchString);
		} else {
			LOG.debug("String [{}] was not a valid UUID. Search cancelled", searchString);
			return SearchResult.invalidUUID(searchString);
		}
	}

	private boolean isPossibleUUID(String searchString) {
		boolean isId = searchString.length() >= 4; // at least 4 digits
		isId &= searchString.matches("[\\-0-9a-fA-F]+"); // only hex digits or minus sign
		return isId;
	}

	private SearchResult processResults(Collection<RegistryElement> searchResults, String searchString) {
		if (searchResults.size() > 1) {
			LOG.debug("Found multiple matches for String [{}]", searchString);
			return SearchResult.multipleResults(searchResults.size(), searchString);
		} else if (searchResults.size() == 1) {
			LOG.debug("Found one match for String [{}]", searchString);
			return SearchResult.oneResult(searchResults.iterator().next());
		} else {
			LOG.debug("Found no matches for String [{}]", searchString);
			return SearchResult.noResult(searchString);
		}

	}

}
