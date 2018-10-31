package de.herglotz.uuid.ui;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectWorkspaceIcon extends MenuItem {

	private static final long serialVersionUID = -1577055642923363355L;

	private static final Logger LOG = LoggerFactory.getLogger(SelectWorkspaceIcon.class);

	private Consumer<Set<File>> updateListener;

	public SelectWorkspaceIcon(Consumer<Set<File>> updateListener) {
		super("Select Workspace");
		this.updateListener = updateListener;
		addActionListener(this::selectWorkspace);
	}

	private void selectWorkspace(ActionEvent event) {
		File selectedFile = selectFile();
		Set<File> files = gatherRuletreeFilesRecursively(selectedFile);
		updateListener.accept(files);
	}

	private Set<File> gatherRuletreeFilesRecursively(File selectedFile) {
		try {
			return Files.walk(selectedFile.toPath())//
					.map(Path::toFile)//
					.filter(File::isFile)//
					.filter(f -> f.getName().endsWith(".ruletree"))//
					.collect(Collectors.toSet());
		} catch (IOException e) {
			LOG.error("Failed to walk directories");
			return new HashSet<>();
		}
	}

	private File selectFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Ruletree", "ruletree"));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(false);
		chooser.showOpenDialog(null);
		File selectedFile = chooser.getSelectedFile();
		return selectedFile;
	}

}
