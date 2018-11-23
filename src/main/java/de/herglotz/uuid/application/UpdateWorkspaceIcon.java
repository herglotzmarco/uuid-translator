package de.herglotz.uuid.application;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

import de.herglotz.uuid.settings.Settings;

class UpdateWorkspaceIcon extends SelectWorkspaceIcon {

	private static final long serialVersionUID = -1577055642923363355L;

	public UpdateWorkspaceIcon(Settings settings, Consumer<Set<File>> updateListener) {
		super("Update Workspace", settings, updateListener);
	}

	@Override
	protected File selectFile() {
		if (settings.getLastWorkspace() != null && new File(settings.getLastWorkspace()).exists()) {
			return new File(settings.getLastWorkspace());
		}
		return super.selectFile();
	}

}
