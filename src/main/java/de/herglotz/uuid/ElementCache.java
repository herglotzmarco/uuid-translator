package de.herglotz.uuid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ElementCache {

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

	public SearchResult findElementContaining(String content) {
		String element = elements.get(content);
		if (element != null) {
			return SearchResult.ok(element);
		}
		List<String> possibilities = elements.entrySet().parallelStream()//
				.filter(e -> e.getKey().contains(content))//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
		if (possibilities.size() > 1) {
			return SearchResult.multipleResults(possibilities.size(), content);
		} else if (possibilities.size() == 1) {
			return SearchResult.ok(possibilities.get(0));
		} else {
			return SearchResult.noResult(content);
		}
	}

}