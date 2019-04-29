package de.herglotz.uuid.application;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import de.herglotz.uuid.settings.Settings;
import de.herglotz.uuid.ui.SettingsCheckbox;
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
		dialog.setLayout(new GridLayout(5, 2));

		createUUIDSearchHotkeySetting(dialog);
		createNameSearchHotkeySetting(dialog);
		createUUIDReplaceHotkeySetting(dialog);
		createNameReplaceHotkeySetting(dialog);
		createShowTypeCheckbox(dialog);
		dialog.pack();
	}

	private void createUUIDSearchHotkeySetting(JDialog dialog) {
		dialog.add(new Label("UUID Search Hotkey"));
		dialog.add(new SettingsChooser(settings::getUUIDSearchHotkey, settings::setUUIDSearchHotkey, updateCallback));
	}

	private void createNameSearchHotkeySetting(JDialog dialog) {
		dialog.add(new Label("Name Search Hotkey"));
		dialog.add(new SettingsChooser(settings::getNameSearchHotkey, settings::setNameSearchHotkey, updateCallback));
	}

	private void createUUIDReplaceHotkeySetting(JDialog dialog) {
		dialog.add(new Label("UUID Replace Hotkey"));
		dialog.add(new SettingsChooser(settings::getUUIDReplaceHotkey, settings::setUUIDReplaceHotkey, updateCallback));
	}

	private void createNameReplaceHotkeySetting(JDialog dialog) {
		dialog.add(new Label("Name Replace Hotkey"));
		dialog.add(new SettingsChooser(settings::getNameReplaceHotkey, settings::setNameReplaceHotkey, updateCallback));
	}

	private void createShowTypeCheckbox(JDialog dialog) {
		dialog.add(new Label("Show Element Type"));
		dialog.add(new SettingsCheckbox(settings::isShowType, settings::setShowType));
	}

}
