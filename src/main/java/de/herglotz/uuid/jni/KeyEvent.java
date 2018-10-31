package de.herglotz.uuid.jni;

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

}
