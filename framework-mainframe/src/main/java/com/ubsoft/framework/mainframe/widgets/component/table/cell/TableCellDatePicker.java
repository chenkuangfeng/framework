package com.ubsoft.framework.mainframe.widgets.component.table.cell;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

import com.borland.dbswing.JEditableComponent;
import com.borland.dx.dataset.Column;
import com.evangelsoft.swing.JDatePicker;

public class TableCellDatePicker extends JDatePicker implements JEditableComponent {
	private static final long serialVersionUID = -4795099076768898474L;

	public TableCellDatePicker(final Column paramColumn) {	
		addPropertyChangeListener("date", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
				java.util.Date localDate = (java.util.Date) paramAnonymousPropertyChangeEvent.getNewValue();
				if (localDate != null) {
					paramColumn.getDataSet().setDate(paramColumn.getColumnName(),
							new java.sql.Date(localDate.getTime()));
				} else {
					paramColumn.getDataSet().setAssignedNull(paramColumn.getColumnName());
				}
			}
		});
	}

	public Object getValue() {
		java.util.Date localDate = getDate(false);
		if (localDate == null) {
			return null;
		}
		return new java.sql.Date(localDate.getTime());
	}

	public void setValue(Object paramObject) {
		if (paramObject == null) {
			setDate(null);
		} else {
			setDate(new java.util.Date(((java.sql.Date) paramObject).getTime()));
		}
	}

	public JTextComponent getTextEditor() {
		return ((JSpinner.DefaultEditor) getSpinner().getEditor()).getTextField();
	}

	public boolean isEditValid() {
		return ((JSpinner.DefaultEditor) getSpinner().getEditor()).getTextField().isEditValid();
	}

	public void commitEdit() throws ParseException {
		((JSpinner.DefaultEditor) getSpinner().getEditor()).getTextField().commitEdit();
	}

	
}
