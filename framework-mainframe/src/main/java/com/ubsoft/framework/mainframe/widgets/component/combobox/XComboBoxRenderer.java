package com.ubsoft.framework.mainframe.widgets.component.combobox;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class XComboBoxRenderer extends BasicComboBoxRenderer {


	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null) {
			ValueText item = (ValueText) value;
			setText(item.getText());
		}
		return this;
	}

}
