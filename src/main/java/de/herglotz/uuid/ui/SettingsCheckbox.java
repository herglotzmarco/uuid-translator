package de.herglotz.uuid.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JCheckBox;

public class SettingsCheckbox extends JCheckBox {

	private static final long serialVersionUID = 6729051379708326076L;

	private Consumer<Boolean> setter;

	public SettingsCheckbox(Supplier<Boolean> getter, Consumer<Boolean> setter) {
		this.setter = setter;
		addActionListener(this::selectionChanged);
		setSelected(getter.get());
	}

	private void selectionChanged(ActionEvent event) {
		setter.accept(isSelected());
	}

}
