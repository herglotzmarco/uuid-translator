package de.herglotz.uuid;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.util.UUID;

public class SelectWorkspaceIcon extends MenuItem {

	private static final long serialVersionUID = -1577055642923363355L;

	private final ElementCache elementCache;

	public SelectWorkspaceIcon(ElementCache elementCache) {
		super("Select Workspace");
		this.elementCache = elementCache;
		addActionListener(this::selectWorkspace);
	}

	private void selectWorkspace(ActionEvent event) {
		elementCache.clear();
		elementCache.put(UUID.randomUUID().toString(), "Blubb");
		elementCache.put(UUID.randomUUID().toString(), "Bla");
		elementCache.put(UUID.randomUUID().toString(), "Fasel");
	}

}
