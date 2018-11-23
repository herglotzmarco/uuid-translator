package de.herglotz.uuid.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.uuid.jni.KeyEvent;

public class Settings {

	private static final String FILE = "settings.properties";

	private static final Logger LOG = LoggerFactory.getLogger(Settings.class);

	private static final String SEARCH_HOTKEY = "Search_Hotkey";
	private static final String REPLACE_HOTKEY = "Replace_Hotkey";
	private static final String SHOW_TYPE = "Show_Type";
	private static final String LAST_WORKSPACE = "Last_Workspace";

	private Properties properties;

	public Settings() {
		properties = getDefaultProperties();
		try (InputStream in = new FileInputStream(FILE)) {
			properties.load(in);
		} catch (IOException e) {
			LOG.error("Reading settings file failed. Using defaults");
		}
	}

	private Properties getDefaultProperties() {
		Properties properties = new Properties();
		properties.setProperty(SEARCH_HOTKEY, new KeyEvent(true, false, "C").toString());
		properties.setProperty(REPLACE_HOTKEY, new KeyEvent(true, false, "R").toString());
		properties.setProperty(SHOW_TYPE, "false");
		return properties;
	}

	public KeyEvent getSearchHotkey() {
		return KeyEvent.fromString(properties.getProperty(SEARCH_HOTKEY));
	}

	public KeyEvent getReplaceHotkey() {
		return KeyEvent.fromString(properties.getProperty(REPLACE_HOTKEY));
	}

	public boolean isShowType() {
		return Boolean.parseBoolean(properties.getProperty(SHOW_TYPE));
	}

	public String getLastWorkspace() {
		return properties.getProperty(LAST_WORKSPACE);
	}

	public void setLastWorkspace(String lastWorkspace) {
		LOG.info("Setting last workspace to [{}]", lastWorkspace);
		properties.setProperty(LAST_WORKSPACE, lastWorkspace);
		save();
	}

	public void setSearchHotkey(KeyEvent key) {
		LOG.info("Setting search hotkey to [{}]", key);
		properties.setProperty(SEARCH_HOTKEY, key.toString());
		save();
	}

	public void setReplaceHotkey(KeyEvent key) {
		LOG.info("Setting replace hotkey to [{}]", key);
		properties.setProperty(REPLACE_HOTKEY, key.toString());
		save();
	}

	public void setShowType(boolean showType) {
		LOG.info("Setting show type to [{}]", showType);
		properties.setProperty(SHOW_TYPE, String.valueOf(showType));
		save();
	}

	private void save() {
		try (OutputStream out = new FileOutputStream(FILE)) {
			properties.store(out, "");
		} catch (IOException e) {
			LOG.error("Failed to save settings");
		}
	}

}
