package de.herglotz.uuid.application;

import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ExitIcon extends MenuItem implements ActionListener {

	private static final long serialVersionUID = -1577055642923363355L;
	private TrayIcon trayIcon;

	public ExitIcon(TrayIcon trayIcon) {
		super("Exit");
		this.trayIcon = trayIcon;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SystemTray.getSystemTray().remove(trayIcon);
		System.exit(0);
	}

}
