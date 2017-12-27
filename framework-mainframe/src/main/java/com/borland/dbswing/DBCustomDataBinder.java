package com.borland.dbswing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import com.borland.dbswing.DBColumnAwareSupport;
import com.borland.dbswing.DBExceptionHandler;
import com.borland.dbswing.DBUtilities;
import com.borland.dbswing.JEditableComponent;
import com.borland.dx.dataset.AccessEvent;
import com.borland.dx.dataset.AccessListener;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ColumnAware;
import com.borland.dx.dataset.DataChangeEvent;
import com.borland.dx.dataset.DataChangeListener;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Designable;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.NavigationListener;
import com.borland.dx.dataset.Variant;
import com.evangelsoft.swing.Editable;

public class DBCustomDataBinder implements FocusListener, AccessListener, DataChangeListener, NavigationListener, PropertyChangeListener, ColumnAware, Designable, Serializable {
	private static final long serialVersionUID = -1743608587770465501L;
	private JComponent comp;
	private DBColumnAwareSupport colAware = new DBColumnAwareSupport(this);
	private boolean ǀ = true;
	private boolean isModified;
	private boolean ƽ = false;
	private boolean ǂ;
	private boolean ǁ = true;

	public DBCustomDataBinder() {
		this(null);
	}

	public DBCustomDataBinder(JComponent comp) {
		setJComponent(comp);
		if ((comp instanceof JEditableComponent)) {
			JTextComponent textComp = ((JEditableComponent) comp).getTextEditor();

			textComp.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent keyEvent) {
					if ((keyEvent.getKeyCode() == 10) && (isNextFocusOnEnter())) {
						if (isModified()) {
							postValue();
						}
					}
				}
			});
		}
	}

	public JComponent getJComponent() {
		return comp;
	}

	public void setJComponent(JComponent paramJComponent) {
		if (comp == paramJComponent) {
			comp.removePropertyChangeListener(this);
			comp.removeFocusListener(this);
		}
		comp = paramJComponent;
		if (paramJComponent != null) {
			paramJComponent.addPropertyChangeListener(this);
			paramJComponent.addFocusListener(this);
		}
	}

	public void postValue() {
		try {
			postValue2();
		} catch (Exception ex) {
			DBExceptionHandler.handleException(this.colAware.getDataSet(), comp, ex);
			comp.requestFocus();
		}
	}

	public void postValue2() throws Exception {
		if ((isModified()) && (this.colAware.isValidDataSetState()) && ((comp instanceof JEditableComponent))) {
			this.isModified = true;
			try {
				this.colAware.setVariant((Variant) ((JEditableComponent) comp).getValue());
			} finally {
				this.isModified = false;
			}
			setModified(false);
		}
	}

	public void updateValue() {
		if (this.isModified)
			return;
		this.isModified = true;
		try {
			if ((this.colAware.isValidDataSetState()) && ((comp instanceof JEditableComponent)))
				((JEditableComponent) comp).setValue(this.colAware.getVariant());
		} catch (Exception localException) {
			DBExceptionHandler.handleException(this.colAware.getDataSet(), comp, localException);
		}
		setModified(false);
		this.isModified = false;
	}

	public void setDataSet(DataSet paramDataSet) {
		if (this.colAware.getDataSet() != null)
			this.colAware.getDataSet().removeNavigationListener(this);
		this.colAware.setDataSet(paramDataSet);
		if (this.colAware.getDataSet() != null) {
			this.colAware.getDataSet().addNavigationListener(this);
			Ü();
		}
	}

	public DataSet getDataSet() {
		return this.colAware.getDataSet();
	}

	public void setColumnName(String paramString) {
		this.colAware.setColumnName(paramString);
		if (paramString != null)
			Ü();
	}

	public String getColumnName() {
		return this.colAware.getColumnName();
	}

	public boolean isModified() {
		return this.ƽ;
	}

	public void setModified(boolean paramBoolean) {
		this.ƽ = paramBoolean;
	}

	public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
		if (this.colAware.getDataSet() != null) {
			if (paramPropertyChangeEvent.getPropertyName().equals("ancestor"))
				Ü();
			else if (paramPropertyChangeEvent.getPropertyName().equals("dataEditing")) {
				if ((!this.isModified) && (!isModified())) {
					boolean bool = this.isModified;
					this.isModified = true;
					try {
						this.colAware.getDataSet().startEdit(this.colAware.getColumn());
						setModified(true);
					} finally {
						this.isModified = bool;
					}
				}
			} else if (paramPropertyChangeEvent.getPropertyName().equals("dataPosted"))
				postValue();
		}
	}

	public void focusGained(FocusEvent paramFocusEvent) {
		DBUtilities.updateCurrentDataSet(comp, this.colAware.getDataSet());
	}

	public void focusLost(FocusEvent paramFocusEvent) {
		if (isModified())
			postValue();
	}

	public void navigated(NavigationEvent paramNavigationEvent) {
		if (!this.ǁ)
			return;
		updateValue();
	}

	public void dataChanged(DataChangeEvent paramDataChangeEvent) {
		if (!this.ǁ)
			return;
		int i = paramDataChangeEvent.getRowAffected();
		int j = (i != this.colAware.getDataSet().getRow()) && (i != -1) ? 0 : 1;
		if ((j != 0) && (!this.isModified))
			updateValue();
	}

	public void postRow(DataChangeEvent paramDataChangeEvent) throws Exception {
		if ((comp instanceof JEditableComponent))
			((JEditableComponent) comp).commitEdit();
		try {
			postValue2();
		} catch (Exception localException) {
			comp.requestFocus();
			throw localException;
		}
	}

	private void Ü() {
		if (Beans.isDesignTime())
			return;
		if ((comp != null) && (comp.isDisplayable())) {
			this.ǂ = false;
			// this.colAware.();
			updateValue();
			if (this.colAware.isValidDataSetState()) {
				Column localColumn = this.colAware.getColumn();
				if ((comp.getBackground() == null) && (localColumn.getBackground() != null))
					comp.setBackground(localColumn.getBackground());
				if ((comp.getForeground() == null) && (localColumn.getForeground() != null))
					comp.setForeground(localColumn.getForeground());
				if ((comp.getFont() == null) && (localColumn.getFont() != null))
					comp.setFont(localColumn.getFont());
				updateValue();
				if (!localColumn.isEditable())
					if ((comp instanceof JTextComponent))
						((JTextComponent) comp).setEditable(false);
					else if ((comp instanceof Editable))
						((Editable) comp).setEditable(false);
			}
		}
	}

	public void accessChange(AccessEvent paramAccessEvent) {
		if (paramAccessEvent.getID() == 2) {
			if (paramAccessEvent.getReason() == 10)
				this.ǁ = false;
			else if (paramAccessEvent.getReason() == 9)
				this.ǂ = true;
		} else if (paramAccessEvent.getReason() == 10) {
			this.ǁ = true;
			updateValue();
		} else if ((paramAccessEvent.getReason() == 1) || (this.ǂ) || (paramAccessEvent.getReason() == 2)) {
			Ü();
		}
	}

	public boolean isNextFocusOnEnter() {
		return this.ǀ;
	}

	public void setNextFocusOnEnter(boolean paramBoolean) {
		this.ǀ = paramBoolean;
	}

}