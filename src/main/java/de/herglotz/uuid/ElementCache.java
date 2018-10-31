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

	public String getResultOrErrorMessage(String content) {
		List<String> possibilities = elements.entrySet().parallelStream()//
				.filter(e -> e.getKey().contains(content))//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
		if (possibilities.size() > 1) {
			return String.format("%s possible results for Id [%s]", possibilities.size(), content);
		} else if (possibilities.size() == 1) {
			return possibilities.get(0);
		} else {
			return String.format("Id containing [%s] is not present in selected workspace", content);
		}
	}

}