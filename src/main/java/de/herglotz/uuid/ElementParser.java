package de.herglotz.uuid;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementParser {

	private static final Logger LOG = LoggerFactory.getLogger(ElementParser.class);

	public static Set<Pair<String, String>> readAndParse(File file) {
		try {
			return Files.lines(file.toPath(), Charset.forName("UTF8"))//
					.map(ElementParser::parseLine)//
					.filter(Optional::isPresent)//
					.map(Optional::get)//
					.collect(Collectors.toSet());
		} catch (IOException e) {
			LOG.error("Failed to read file: {}", file.getAbsolutePath(), e);
			return new HashSet<>();
		}
	}

	private static Optional<Pair<String, String>> parseLine(String line) {
		// TODO: Retain parent?
		// TODO: Implement parsing
		return Optional.empty();
	}

}
