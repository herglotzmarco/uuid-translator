package de.herglotz.uuid.search;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.uuid.elements.ElementRegistry;
import de.herglotz.uuid.elements.RegistryElement;

public class NameSearcher {

	private static final Logger LOG = LoggerFactory.getLogger(NameSearcher.class);

	private ElementRegistry elementContainer;

	public NameSearcher(ElementRegistry elementContainer) {
		this.elementContainer = elementContainer;
	}

	public SearchResult searchForName(String searchString) {
		LOG.debug("Searching for String [{}]", searchString);
		Collection<RegistryElement> searchResults = elementContainer.findElementsWithName(searchString);
		return processResults(searchResults, searchString);
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
