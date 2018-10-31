package de.herglotz.uuid.ui;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SelectWorkspaceIcon extends MenuItem {

	private static final long serialVersionUID = -1577055642923363355L;

	private Consumer<Set<File>> updateListener;

	public SelectWorkspaceIcon(Consumer<Set<File>> updateListener) {
		super("Select Workspace");
		this.updateListener = updateListener;
		addActionListener(this::selectWorkspace);
	}

	private void selectWorkspace(ActionEvent event) {
		// TODO: implement file chooser
		updateListener.accept(new HashSet<>());
	}

}
