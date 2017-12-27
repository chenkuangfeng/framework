package com.ubsoft.framework.mainframe.widgets.component.table.cell;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

import com.borland.dbswing.JEditableComponent;
import com.borland.dx.dataset.Column;
import com.evangelsoft.swing.JDateTimePicker;

public class TableCellDateTimePicker
extends JDateTimePicker
implements JEditableComponent
{
private static final long serialVersionUID = -5796484024797315288L;

public TableCellDateTimePicker(final Column paramColumn)
{
  addPropertyChangeListener("datetime", new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
    {
      java.util.Date localDate = (java.util.Date)paramAnonymousPropertyChangeEvent.getNewValue();
      if (localDate != null) {
    	  paramColumn.getDataSet().setTimestamp(paramColumn.getColumnName(), new Timestamp(localDate.getTime()));
      } else {
    	  paramColumn.getDataSet().setAssignedNull(paramColumn.getColumnName());
      }
    }
  });
}

public Object getValue()
{
  java.util.Date localDate = getDateTime(false);
  if (localDate == null) {
    return null;
  }
  return new Timestamp(localDate.getTime());
}

public void setValue(Object paramObject)
{
  if (paramObject == null) {
    setDateTime(null);
  } else {
    setDateTime(new java.util.Date(((Timestamp)paramObject).getTime()));
  }
}

public JTextComponent getTextEditor()
{
  return ((JSpinner.DefaultEditor)getSpinner().getEditor()).getTextField();
}

public boolean isEditValid()
{
  return ((JSpinner.DefaultEditor)getSpinner().getEditor()).getTextField().isEditValid();
}

public void commitEdit()
  throws ParseException
{
  ((JSpinner.DefaultEditor)getSpinner().getEditor()).getTextField().commitEdit();
}
}
