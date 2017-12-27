package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.borland.dbswing.JdbTextField;
import com.borland.dx.dataset.ColumnAware;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.ConditionNode;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.event.LookupListener;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.mainframe.widgets.util.DataSetUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.metadata.model.widget.LookupFieldMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.system.common.SelectDialog;

public class XLookupField extends JPanel implements IRenderer, ActionListener, ColumnAware, Serializable {

	transient LookupListener lookupListener;
	private DataSet dataSet;
	private String columnName;
	private JButton button = new JButton();
	private JdbTextField textField = new JdbTextField();

	/** 弹出框默认过滤条件 **/
	private List<ConditionNode> condiitionNode;

	public List<ConditionNode> getCondiitionNode() {
		return condiitionNode;
	}

	public void setCondiitionNode(List<ConditionNode> condiitionNode) {
		this.condiitionNode = condiitionNode;
	}

	public XLookupField() {
		button.addActionListener(this);
		button.setFocusable(false);
		button.setFocusPainted(false);
		button.setIcon(IconFactory.getImageIcon("icon/find.png"));
		button.setMargin(new Insets(0, 0, 0, 0));
		// JPanel panel = new JPanel();
		this.setLayout(new BorderLayout());
		textField.setColumns(8);
		this.add(textField);
		this.add(button, "East");
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		this.textField.setDataSet(dataSet);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
		this.textField.setColumnName(columnName);
	}

	public String getText() {
		return textField.getText();
	}
	public void setText(String text) {
		 textField.setText(text);
	}
	
	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public JdbTextField getTextField() {
		return textField;
	}

	public void setTextField(JdbTextField textField) {
		this.textField = textField;
	}

	public LookupListener getLookupListener() {
		return lookupListener;
	}

	public void addLookupListener(LookupListener lookupListener) {
		this.lookupListener = lookupListener;
	}

	/**
	 * 重写基类，默认自动增加列
	 * 
	 * @param dataSet
	 */
	public void setDateSet(DataSet dataSet) {
		if (this.getColumnName() != null) {

			if (dataSet.getColumn(this.getColumnName()) == null) {
				XColumn xcol = new XColumn();
				xcol.setColumnName(this.getColumnName());
				xcol.setDataType(Variant.STRING);
				dataSet.getStorageDataSet().addColumn(xcol);
			}
		}
		this.setDataSet(dataSet);
	}

	@SuppressWarnings("unused")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.dataSet != null && columnName != null) {
			if (meta.getSelect() != null) {
				List<Bio> result = SelectDialog.select(meta.getSelect(), this, condiitionNode, false);
			
				if (result != null) {
					Bio bio = result.get(0);
					String fromKey = meta.getSelectField() == null ? meta.getField() : meta.getSelectField();

					Variant varField = new Variant();
					DataSetUtil.setVariant(varField, dataSet.getColumn(columnName), bio.getObject(fromKey.toUpperCase()));
					dataSet.setVariant(columnName, varField);
					if (meta.getFromFields() != null && meta.getToFields() != null) {
						String[] fFields = meta.getFromFields().split(",");
						String[] tFields = meta.getToFields().split(",");
						for (int i = 0; i < fFields.length; i++) {
							Variant var = new Variant();
							DataSetUtil.setVariant(var, dataSet.getColumn(tFields[i]), bio.getObject(fFields[i].toUpperCase()));
							dataSet.setVariant(tFields[i], var);
						}
					}
					if (this.getLookupListener() != null) {
						getLookupListener().lookup(this, result);
					}
				}
			}

		} else {
			if (meta.getSelect() != null) {
				List<Bio> result = SelectDialog.select(meta.getSelect(), this, condiitionNode, true);
				if (result != null) {
					String fromKey = meta.getSelectField() == null ? meta.getField() : meta.getSelectField();

					String text = "";
					for (Bio bio : result) {
						String value = bio.getString(fromKey.toUpperCase());
						if (value != null) {
							text += value + ";";
						}
					}
					if (StringUtil.isNotEmpty(text)) {
						text = text.substring(0, text.length() - 1);
						textField.setText(text);
					}
					if (this.getLookupListener() != null) {
						getLookupListener().lookup(this, result);
					}
				}
			}

		}
	}

	LookupFieldMeta meta;

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		LookupFieldMeta lkpMeta = (LookupFieldMeta) meta;
		this.meta = lkpMeta;
		if (meta.getLabel() != null) {
			RendererUtil.addLabelField(meta, this, parent, params);
		} else {
			RendererUtil.addComponent(meta, this, parent, params);
		}

		return this;
	}
public void setEnabled(boolean enabled){
	button.setEnabled(enabled);
	textField.setEnabled(enabled);
}
	@Override
	public LookupFieldMeta getMeta() {
		// TODO Auto-generated method stub
		return meta;
	}

}
