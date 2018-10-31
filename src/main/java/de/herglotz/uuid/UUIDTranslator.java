package de.herglotz.uuid;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JFrame;

import de.herglotz.uuid.jni.GlobalKeyListener;
import de.herglotz.uuid.jni.KeyEvent;

public class UUIDTranslator {

	public UUIDTranslator() {
		createFrame();
		registerListener();
	}

	private void createFrame() {
		JFrame mainFrame = new JFrame("UUID Translator");
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setSize(400, 200);
	}

	private void registerListener() {
		GlobalKeyListener listener = GlobalKeyListener.instance();
		listener.registerListener(new KeyEvent(true, false, "C"), this::searchForId);
	}

	private void searchForId() {
		String content = getClipboardContent();
		System.out.println(content);
	}

	private String getClipboardContent() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			return clipboard.getData(DataFlavor.stringFlavor).toString();
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
