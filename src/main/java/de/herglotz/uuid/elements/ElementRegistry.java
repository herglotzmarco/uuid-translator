package de.herglotz.uuid.elements;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(ElementRegistry.class);

	private Map<String, RegistryElement> elements;
	private ElementParser parser;

	public ElementRegistry() {
		elements = new HashMap<>();
		parser = null;
	}

	public Collection<RegistryElement> findElementsContaining(String searchString) {
		RegistryElement element = elements.get(searchString);
		if (element != null) {
			return Collections.singleton(element);
		}
		return elements.entrySet().parallelStream()//
				.filter(e -> e.getKey().contains(searchString))//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
	}

	public void updateElements(Set<File> files) {
		LOG.debug("Updating registry for new workspace");
		elements.clear();
		files.stream()//
				.map(parser::readAndParse)//
				.flatMap(Collection::stream)//
				.forEach(e -> elements.put(e.getId(), e));
		LOG.debug("Updating registry done");
	}

}