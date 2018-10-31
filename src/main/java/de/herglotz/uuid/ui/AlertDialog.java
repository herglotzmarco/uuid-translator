package de.herglotz.uuid.ui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

public class AlertDialog {

	private JDialog dialog;

	public AlertDialog() {
		createDialog();
		moveDialogToBottomRight();
	}

	private void createDialog() {
		dialog = new JDialog();
		dialog.setVisible(false);
		dialog.setUndecorated(true);
		dialog.setSize(200, 100);
		dialog.setAlwaysOnTop(true);
	}

	private void moveDialogToBottomRight() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - dialog.getWidth();
		int y = (int) rect.getMaxY() - dialog.getHeight();
		dialog.setLocation(x, y - 45);
	}

	public void showPopup(String name) {
		JLabel label = new JLabel(name);
		dialog.add(label);
		dialog.setVisible(true);
		Timer timer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.remove(label);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

}
