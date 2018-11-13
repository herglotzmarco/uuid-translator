package de.herglotz.uuid.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AlertDialog {

	private static final int ALERT_WIDTH = 300;
	private static final int ALERT_HEIGHT = 150;
	private static final int ALERT_TIMEOUT_MILLIS = 3000;

	private JDialog dialog;

	public AlertDialog() {
		createDialog();
		moveDialogToBottomRight();
		styleDialog();
	}

	private void createDialog() {
		dialog = new JDialog();
		dialog.setVisible(false);
		dialog.setUndecorated(true);
		dialog.setSize(ALERT_WIDTH, ALERT_HEIGHT);
		dialog.setAlwaysOnTop(true);
		dialog.setLayout(new FlowLayout());
	}

	private void moveDialogToBottomRight() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - dialog.getWidth();
		int y = (int) rect.getMaxY() - dialog.getHeight();
		dialog.setLocation(x - 5, y - 45);
	}

	private void styleDialog() {
		JPanel contentPane = (JPanel) dialog.getContentPane();
		contentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.setBackground(new Color(200, 255, 255));
	}

	public void showMessage(String message) {
		showPopup(message); // TODO: handle message differently from error
	}

	public void showError(String error) {
		showPopup(error); // TODO: handle message differently from error
	}

	private void showPopup(String name) {
		JLabel label = new JLabel(toReadableName(name));
		dialog.add(label);

		dialog.setVisible(true);
		Timer timer = new Timer(ALERT_TIMEOUT_MILLIS, e -> {
			dialog.remove(label);
			dialog.validate();
			if (dialog.getContentPane().getComponentCount() == 0) {
				dialog.setVisible(false);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	private String toReadableName(String name) {
		String[] parts = name.split("/");
		StringBuilder builder = new StringBuilder(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			builder.append("<br/>");
			IntStream.range(0, i).forEach(j -> builder.append("- "));
			builder.append(parts[i]);
		}
		return wrapText(builder.toString());
	}

	private String wrapText(String name) {
		return "<html><body style='width:" + (ALERT_WIDTH - 100) + "px'><p style='white-space: pre-wrap'>" + name
				+ "</p></body></html>";
	}

}
