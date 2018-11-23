package de.herglotz.uuid.application;

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

import de.herglotz.uuid.settings.Settings;

class SelectWorkspaceIcon extends MenuItem {

	private static final long serialVersionUID = -1577055642923363355L;

	private static final Logger LOG = LoggerFactory.getLogger(SelectWorkspaceIcon.class);

	private Consumer<Set<File>> updateListener;
	protected Settings settings;

	protected SelectWorkspaceIcon(String title, Settings settings, Consumer<Set<File>> updateListener) {
		super(title);
		this.settings = settings;
		this.updateListener = updateListener;
		addActionListener(this::selectWorkspace);
	}

	public SelectWorkspaceIcon(Settings settings, Consumer<Set<File>> updateListener) {
		this("Select Workspace", settings, updateListener);
	}

	private void selectWorkspace(ActionEvent event) {
		File selectedFile = selectFile();
		if (selectedFile != null && selectedFile.exists()) {
			Set<File> files = gatherRuletreeFilesRecursively(selectedFile);
			settings.setLastWorkspace(selectedFile.getAbsolutePath());
			updateListener.accept(files);
		}
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

	protected File selectFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Ruletree", "ruletree"));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(false);
		if (settings.getLastWorkspace() != null) {
			chooser.setCurrentDirectory(new File(settings.getLastWorkspace()));
		}
		chooser.showOpenDialog(null);
		File selectedFile = chooser.getSelectedFile();
		return selectedFile;
	}

}
