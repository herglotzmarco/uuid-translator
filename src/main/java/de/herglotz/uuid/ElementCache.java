package de.herglotz.uuid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementCache {

	private static final Logger LOG = LoggerFactory.getLogger(ElementCache.class);

	private Map<String, String> elements;

	public ElementCache() {
		elements = new HashMap<>();
	}

	public void clear() {
		elements.clear();
	}

	public void put(String id, String name) {
		elements.put(id, name);
	}

	public SearchResult findElementContaining(String searchString) {
		String element = elements.get(searchString);
		if (element != null) {
			LOG.debug("Found perfect match for String [{}]", searchString);
			return SearchResult.ok(element);
		}
		List<String> possibilities = elements.entrySet().parallelStream()//
				.filter(e -> e.getKey().contains(searchString))//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
		if (possibilities.size() > 1) {
			LOG.debug("Found multiple matches for String [{}]", searchString);
			return SearchResult.multipleResults(possibilities.size(), searchString);
		} else if (possibilities.size() == 1) {
			LOG.debug("Found one match for String [{}]", searchString);
			return SearchResult.ok(possibilities.get(0));
		} else {
			LOG.debug("Found no matches for String [{}]", searchString);
			return SearchResult.noResult(searchString);
		}
	}

}