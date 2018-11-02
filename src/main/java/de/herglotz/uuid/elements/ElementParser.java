package de.herglotz.uuid.elements;

import java.io.File;
import java.util.Set;

public interface ElementParser {

	Set<RegistryElement> readAndParse(File file);

}
