package de.herglotz.uuid.application;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import de.herglotz.uuid.settings.Settings;
import de.herglotz.uuid.ui.SettingsChooser;

class SettingsIcon extends MenuItem {

	private static final long serialVersionUID = 6833525492909578116L;

	private Settings settings;
	private Runnable updateCallback;

	public SettingsIcon(Settings settings, Runnable updateCallback) {
		super("Settings");
		this.settings = settings;
		this.updateCallback = updateCallback;
		addActionListener(this::openSettings);
	}

	private void openSettings(ActionEvent event) {
		JDialog dialog = new JDialog();
		dialog.setTitle("Settings");
		dialog.setVisible(true);
		dialog.setLayout(new GridLayout(2, 2));

		createSearchHotkeySetting(dialog);
		createReplaceHotkeySetting(dialog);
		dialog.pack();
	}

	private void createSearchHotkeySetting(JDialog dialog) {
		dialog.add(new Label("Search Hotkey"));
		dialog.add(new SettingsChooser(settings::getSearchHotkey, settings::setSearchHotkey, updateCallback));
	}

	private void createReplaceHotkeySetting(JDialog dialog) {
		dialog.add(new Label("Replace Hotkey"));
		dialog.add(new SettingsChooser(settings::getReplaceHotkey, settings::setReplaceHotkey, updateCallback));
	}

}
