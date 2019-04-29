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

	private Map<String, RegistryElement> idsToElements;
	private Map<String, RegistryElement> namesToElements;
	private ElementParser parser;

	public ElementRegistry() {
		idsToElements = new HashMap<>();
		namesToElements = new HashMap<>();
		parser = null;
	}

	public Collection<RegistryElement> findElementsWithId(String searchString) {
		RegistryElement element = idsToElements.get(searchString);
		if (element != null) {
			return Collections.singleton(element);
		}
		return idsToElements.entrySet().parallelStream()//
				.filter(e -> e.getKey().contains(searchString))//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
	}

	public Collection<RegistryElement> findElementsWithName(String searchString) {
		RegistryElement element = namesToElements.get(searchString);
		if (element != null) {
			return Collections.singleton(element);
		}
		return namesToElements.entrySet().parallelStream()//
				.filter(e -> e.getKey().contains(searchString))//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
	}

	public void updateElements(Set<File> files) {
		LOG.debug("Updating registry for new workspace");
		idsToElements.clear();
		files.stream()//
				.map(parser::readAndParse)//
				.flatMap(Collection::stream)//
				.forEach(this::addElement);
		LOG.debug("Updating registry done");
	}

	private void addElement(RegistryElement e) {
		idsToElements.put(e.getId(), e);
		namesToElements.put(e.getName(), e);
	}

}