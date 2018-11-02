package de.herglotz.uuid.jni;

import org.jnativehook.keyboard.NativeKeyEvent;

public class KeyEvent {

	private boolean ctrlPressed;
	private boolean shiftPressed;
	private String key;

	public KeyEvent(boolean ctrlPressed, boolean shiftPressed, String key) {
		this.ctrlPressed = ctrlPressed;
		this.shiftPressed = shiftPressed;
		this.key = key;
	}

	public boolean isCtrlPressed() {
		return ctrlPressed;
	}

	public boolean isShiftPressed() {
		return shiftPressed;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ctrlPressed ? 1231 : 1237);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + (shiftPressed ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyEvent other = (KeyEvent) obj;
		if (ctrlPressed != other.ctrlPressed)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (shiftPressed != other.shiftPressed)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return (ctrlPressed ? "Ctrl+" : "") + (shiftPressed ? "Shift+" : "") + key;
	}

	public static KeyEvent fromNativeKeyEvent(NativeKeyEvent nativeEvent) {
		boolean ctrlPressed = (nativeEvent.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0;
		boolean shiftPressed = (nativeEvent.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0;
		return new KeyEvent(ctrlPressed, shiftPressed, NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
	}

	public static KeyEvent fromString(String string) {
		String[] split = string.split("\\+");
		boolean ctrl = false;
		boolean shift = false;
		String key = "";
		for (String part : split) {
			if (part.equals("Ctrl")) {
				ctrl = true;
				key = "Ctrl";
			} else if (part.equals("Shift")) {
				shift = true;
				key = "Shift";
			} else {
				key = part;
			}
		}
		return new KeyEvent(ctrl, shift, key);
	}

}
