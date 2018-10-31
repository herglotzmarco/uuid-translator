package de.herglotz.uuid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

	public Optional<String> get(String id) {
		return Optional.ofNullable(elements.get(id));
	}

}
