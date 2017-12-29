package com.ubsoft.framework.mainframe.widgets.util;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.PickListDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.ConditionLeafNode;
import com.ubsoft.framework.core.dal.model.ConditionNode;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.widgets.component.XDateField;
import com.ubsoft.framework.mainframe.widgets.component.XDateTimeField;
import com.ubsoft.framework.mainframe.widgets.component.XLookupField;
import com.ubsoft.framework.mainframe.widgets.component.XTextArea;
import com.ubsoft.framework.mainframe.widgets.component.XTextField;
import com.ubsoft.framework.mainframe.widgets.component.combobox.ValueText;
import com.ubsoft.framework.mainframe.widgets.component.combobox.XComboBox;
import com.ubsoft.framework.mainframe.widgets.component.tree.TreeNodeModel;
import com.ubsoft.framework.metadata.model.widget.ComboBoxMeta;
import com.ubsoft.framework.metadata.model.widget.DateFieldMeta;
import com.ubsoft.framework.metadata.model.widget.DateTimeFieldMeta;
import com.ubsoft.framework.metadata.model.widget.InputFieldMeta;

public class FormUtil {

	private static ConditionLeafNode getConditionLeafNode(InputFieldMeta meta, Component comp) {
		String op = meta.getOp();
		String as = meta.getAs();
		String field = meta.getField();
		String join = meta.getJoin() == null ? "and" : meta.getJoin();
		String dataType = meta.getDataType();
		Object value = null;
		if (comp instanceof XTextField) {
			XTextField textField = (XTextField) comp;
			if (op == null) {
				op = "like";
			}
			value = textField.getText().trim();
		} else if (comp instanceof XComboBox) {
			XComboBox combox = (XComboBox) comp;
			if (combox.getSelectedIndex() != -1) {
				ValueText vt = (ValueText) combox.getSelectedItem();
				value = vt.getValue();
			}

		} else if (comp instanceof XDateField) {
			XDateField dataField = (XDateField) comp;
			value = dataField.getDate();
		} else if (comp instanceof XDateTimeField) {
			XDateTimeField dataField = (XDateTimeField) comp;
			value = dataField.getDateTime();
		} else if (comp instanceof XLookupField) {
			XLookupField lookupField = (XLookupField) comp;
			String text = lookupField.getText();
			if (StringUtil.isNotEmpty(text)) {
				String[] textArray = text.split(";");
				if (textArray.length > 1) {
					value = textArray;

				} else {
					value = textArray[0];
				}

			}

		}
		if (op == null) {
			op = "=";
		}
		if (StringUtil.isNotEmpty(value)) {
			ConditionLeafNode leafNode = new ConditionLeafNode(as, field, op, value, null, join);
			return leafNode;
		}
		return null;
	}

	/**
	 * 获取查询条件
	 * 
	 * @param container
	 * @param conditionNode
	 */
	public static void getQueryCondition(Container container, List<ConditionNode> conditionNode) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof Container) {
				if (comp instanceof XTextField) {
					XTextField textField = (XTextField) comp;
					ConditionLeafNode leafNode = FormUtil.getConditionLeafNode(textField.getMeta(), textField);
					if (leafNode != null) {
						conditionNode.add(leafNode);
					}
				} else if (comp instanceof XComboBox) {
					XComboBox combox = (XComboBox) comp;
					ConditionLeafNode leafNode = FormUtil.getConditionLeafNode(combox.getMeta(), combox);
					if (leafNode != null) {
						conditionNode.add(leafNode);
					}
				} else if (comp instanceof XDateField) {
					XDateField dataField = (XDateField) comp;
					ConditionLeafNode leafNode = FormUtil.getConditionLeafNode(dataField.getMeta(), dataField);
					if (leafNode != null) {
						conditionNode.add(leafNode);
					}
				} else if (comp instanceof XDateTimeField) {
					XDateTimeField dataField = (XDateTimeField) comp;
					ConditionLeafNode leafNode = FormUtil.getConditionLeafNode(dataField.getMeta(), dataField);
					if (leafNode != null) {
						conditionNode.add(leafNode);
					}
				} else if (comp instanceof XLookupField) {
					XLookupField lookupField = (XLookupField) comp;
					ConditionLeafNode leafNode = FormUtil.getConditionLeafNode(lookupField.getMeta(), lookupField);
					if (leafNode != null) {
						conditionNode.add(leafNode);
					}

				} else {
					getQueryCondition((Container) comp, conditionNode);
				}

			}
		}

	}

	/**
	 * 从组件配置动态添加列
	 * 
	 * @param meta
	 * @param dataSet
	 */
	private static void addDataSetColumn(InputFieldMeta meta, StorageDataSet dataSet) {
		Column column = dataSet.hasColumn(meta.getField());
		if (column == null) {
			column = new Column();
			String dataType = meta.getDataType();
			int variantDataType = -1;
			if (dataType == null) {
				dataType = TypeUtil.STRING;
			}
			if (meta instanceof ComboBoxMeta) {
				ComboBoxMeta cmbMeta = (ComboBoxMeta) meta;
				String code = cmbMeta.getCode();
				Boolean bind = cmbMeta.getBind() == null ? true : cmbMeta.getBind();

				if (code != null && bind) {
					try {
						DataSet ds = LookupManager.getLookupDataSet(code);
						PickListDescriptor pd = new PickListDescriptor(ds, new String[] { "value" }, new String[] { "text" },
								new String[] { meta.getField() }, "text", true);
						column.setPickList(pd);
						column.setWidth(50);
					} catch (Exception ex) {
						MessageBox.showException(ex);
					}

				}

			}
			// 日期类型
			if (meta instanceof DateFieldMeta) {
				dataType = TypeUtil.DATE;
			} else if (meta instanceof DateTimeFieldMeta) {
				dataType = TypeUtil.TIMESTAMP;
			}
			if (dataType.equals(TypeUtil.STRING)) {
				variantDataType = Variant.STRING;
			} else if (dataType.equals(TypeUtil.INTEGER)) {
				variantDataType = Variant.INT;
			} else if (dataType.equals(TypeUtil.DOUBLE)) {
				variantDataType = Variant.DOUBLE;
			} else if (dataType.equals(TypeUtil.BIGDECIMAL)) {
				variantDataType = Variant.BIGDECIMAL;
			} else if (dataType.equals(TypeUtil.LONG)) {
				variantDataType = Variant.LONG;
			} else if (dataType.equals(TypeUtil.SHORT)) {
				variantDataType = Variant.SHORT;
			} else if (dataType.equals(TypeUtil.FLOAT)) {
				variantDataType = Variant.FLOAT;
			} else if (dataType.equals(TypeUtil.DATE)) {
				variantDataType = Variant.DATE;
			} else if (dataType.equals(TypeUtil.TIMESTAMP)) {
				variantDataType = Variant.TIMESTAMP;
			} else if (dataType.equals(TypeUtil.BYTE_ARRAY)) {
				variantDataType = Variant.BYTE_ARRAY;
			} else {
				MessageBox.showError("不支持数据类型:" + dataType);
			}
			column.setDataType(variantDataType);
			column.setColumnName(meta.getField());
			dataSet.addColumn(column);

		}

	}

	public static void setFormEditable(Container container, boolean editable) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof XTextField) {
				XTextField textField = (XTextField) comp;
				textField.setEditable(editable);
				if (textField.getMeta() != null) {
					if (textField.getMeta().getEnabled() != null && textField.getMeta().getEnabled() == false) {
						textField.setEditable(false);
					}
				}
			} else if (comp instanceof XComboBox) {
				XComboBox compField = (XComboBox) comp;
				compField.setEnabled(editable);
				if (compField.getMeta() != null) {
					if (compField.getMeta().getEnabled() != null && compField.getMeta().getEnabled() == false) {
						compField.setEnabled(false);
					}
				}
			} else if (comp instanceof XLookupField) {
				XLookupField lookupField = (XLookupField) comp;
				lookupField.setEnabled(editable);
				if (lookupField.getMeta() != null) {
					if (lookupField.getMeta().getEnabled() != null && lookupField.getMeta().getEnabled() == false) {
						lookupField.setEnabled(false);
					}
				}
			} else {
				if (comp instanceof Container) {
					Container temp = (Container) comp;
					setFormEditable(temp, editable);
				}
			}
		}

	}

	public static void setFormModel(Container form, StorageDataSet dataSet) {
		for (Component comp : form.getComponents()) {
			if (comp instanceof XTextField) {
				XTextField textField = (XTextField) comp;
				if (textField.getMeta().getField() != null) {
					addDataSetColumn(textField.getMeta(), dataSet);
					textField.setDataSet(dataSet);
					textField.setColumnName(textField.getMeta().getField());
				}

			} else if (comp instanceof XComboBox) {
				XComboBox combox = (XComboBox) comp;
				addDataSetColumn(combox.getMeta(), dataSet);
				if (combox.getMeta().getField() != null) {
					combox.setDataSet(dataSet);
					combox.setColumnName(combox.getMeta().getField());
				}

			} else if (comp instanceof XDateField) {
				XDateField dataField = (XDateField) comp;
				addDataSetColumn(dataField.getMeta(), dataSet);

				if (dataField.getMeta().getField() != null) {
					dataField.setDataSet(dataSet);
					dataField.setColumnName(dataField.getMeta().getField());
				}

			} else if (comp instanceof XDateTimeField) {
				XDateTimeField dataField = (XDateTimeField) comp;
				addDataSetColumn(dataField.getMeta(), dataSet);

				if (dataField.getMeta().getField() != null) {
					dataField.setDataSet(dataSet);
					dataField.setColumnName(dataField.getMeta().getField());
				}

			} else if (comp instanceof XLookupField) {
				XLookupField lookupField = (XLookupField) comp;
				addDataSetColumn(lookupField.getMeta(), dataSet);

				if (lookupField.getMeta().getField() != null) {
					lookupField.setDataSet(dataSet);
					lookupField.setColumnName(lookupField.getMeta().getField());
				}

			} else if (comp instanceof XTextArea) {
				XTextArea textField = (XTextArea) comp;
				if (textField.getMeta().getField() != null) {
					addDataSetColumn(textField.getMeta(), dataSet);
					textField.setDataSet(dataSet);
					textField.setColumnName(textField.getMeta().getField());
				}

			} else {

				setFormModel((Container) comp, dataSet);
			}

		}

	}

	/**
	 * 
	 * @param idProperty
	 *            :tree id javabean属性名
	 * @param textProperty
	 *            ：tree text javabean属性名
	 * @param parentIdProperty
	 *            tree 上级ID javabean属性名
	 * @param beans
	 *            ： javabean
	 * @param rootId
	 *            ：tree 根节点固定ID
	 * @param rootText
	 *            ：tree根节点名称
	 * @return
	 */
	public static TreeNodeModel loadTreeModel(String idProperty, String textProperty, String moduleProperty, String parentIdProperty, List beans,
			String rootId, String rootText) {
		TreeNodeModel root = new TreeNodeModel();
		root.setId(rootId);
		root.setText(rootText);
		try {
			builderModel(root, beans, idProperty, textProperty, moduleProperty, parentIdProperty);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}

	private static void builderModel(TreeNodeModel parentNode, List beans, String idProperty, String textProperty, String moduleProperty,
			String parentIdProperty) throws Exception {
		List<TreeNodeModel> childNode = new ArrayList<TreeNodeModel>();
		Class type = beans.get(0).getClass();
		for (Object bean : beans) {
			String id = BeanUtils.getProperty(bean, idProperty);
			String text = BeanUtils.getProperty(bean, textProperty);
			// 只有一级作为Jlist用
			String pid = null;
			if (parentIdProperty == null) {
				pid = "ROOT";
			} else {
				pid = BeanUtils.getProperty(bean, parentIdProperty);
			}
			String module = BeanUtils.getProperty(bean, moduleProperty);
			if (parentNode.getId().equals(pid)) {
				TreeNodeModel item = new TreeNodeModel();
				item.setId(id);
				item.setText(text);
				item.setModule(module);
				childNode.add(item);
				builderModel(item, beans, idProperty, textProperty, moduleProperty, parentIdProperty);
			}
		}
		parentNode.setChildren(childNode);

	}

	public static TreeNodeModel loadTreeModelFormBio(String idProperty, String textProperty, String moduleProperty, String parentIdProperty,
			List<Bio> beans, String rootId, String rootText) {
		TreeNodeModel root = new TreeNodeModel();
		root.setId(rootId);
		root.setText(rootText);
		try {
			builderModel(root, beans, idProperty, textProperty, moduleProperty, parentIdProperty);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}

	private static void builderModelFormBio(TreeNodeModel parentNode, List<Bio> beans, String idProperty, String textProperty, String moduleProperty,
			String parentIdProperty) throws Exception {
		List<TreeNodeModel> childNode = new ArrayList<TreeNodeModel>();
		for (Bio bean : beans) {
			String id = bean.getString(idProperty);
			String text = bean.getString(textProperty);
			String module = bean.getString(moduleProperty);

			// 只有一级作为Jlist用
			String pid = bean.getString(parentIdProperty);
			if (parentNode.getId().equals(pid)) {
				TreeNodeModel item = new TreeNodeModel();
				item.setId(id);
				item.setText(text);
				item.setModule(module);
				childNode.add(item);
				builderModelFormBio(item, beans, idProperty, textProperty, moduleProperty, parentIdProperty);
			}
		}
		parentNode.setChildren(childNode);

	}
}
