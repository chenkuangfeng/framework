
package com.ubsoft.framework.mainframe.widgets.renderer;

import java.util.HashMap;
import java.util.Map;

import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.component.XCheckBox;
import com.ubsoft.framework.mainframe.widgets.component.XDateField;
import com.ubsoft.framework.mainframe.widgets.component.XDateTimeField;
import com.ubsoft.framework.mainframe.widgets.component.XLabel;
import com.ubsoft.framework.mainframe.widgets.component.XLookupField;
import com.ubsoft.framework.mainframe.widgets.component.XPanel;
import com.ubsoft.framework.mainframe.widgets.component.XSplitPanel;
import com.ubsoft.framework.mainframe.widgets.component.XTabPanel;
import com.ubsoft.framework.mainframe.widgets.component.XTextArea;
import com.ubsoft.framework.mainframe.widgets.component.XTextField;
import com.ubsoft.framework.mainframe.widgets.component.XToolBar;
import com.ubsoft.framework.mainframe.widgets.component.combobox.XComboBox;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.component.table.XTable;
import com.ubsoft.framework.mainframe.widgets.component.tree.XTree;
import com.ubsoft.framework.metadata.model.widget.ButtonMeta;
import com.ubsoft.framework.metadata.model.widget.CheckBoxMeta;
import com.ubsoft.framework.metadata.model.widget.ComboBoxMeta;
import com.ubsoft.framework.metadata.model.widget.DateFieldMeta;
import com.ubsoft.framework.metadata.model.widget.DateTimeFieldMeta;
import com.ubsoft.framework.metadata.model.widget.HiddenFieldMeta;
import com.ubsoft.framework.metadata.model.widget.LabelMeta;
import com.ubsoft.framework.metadata.model.widget.LookupFieldMeta;
import com.ubsoft.framework.metadata.model.widget.PanelMeta;
import com.ubsoft.framework.metadata.model.widget.SplitPanelMeta;
import com.ubsoft.framework.metadata.model.widget.TabPanelMeta;
import com.ubsoft.framework.metadata.model.widget.TextAreaMeta;
import com.ubsoft.framework.metadata.model.widget.TextFieldMeta;
import com.ubsoft.framework.metadata.model.widget.ToolBarMeta;
import com.ubsoft.framework.metadata.model.widget.table.TableColumnMeta;
import com.ubsoft.framework.metadata.model.widget.table.TableMeta;
import com.ubsoft.framework.metadata.model.widget.tree.TreeMeta;


@SuppressWarnings("unchecked")
public class MetaRegistry {
	@SuppressWarnings("rawtypes")
	private static final Map registry = new HashMap();
	
	static {		
		registry.put(TreeMeta.class, XTree.class);
		registry.put(LabelMeta.class, XLabel.class);
		registry.put(TextFieldMeta.class, XTextField.class);
		registry.put(TextAreaMeta.class, XTextArea.class);


		registry.put(DateFieldMeta.class, XDateField.class);
		registry.put(DateTimeFieldMeta.class, XDateTimeField.class);
		registry.put(CheckBoxMeta.class, XCheckBox.class);
		registry.put(LookupFieldMeta.class, XLookupField.class);
		registry.put(HiddenFieldMeta.class, XTextField.class);
		
		registry.put(ComboBoxMeta.class, XComboBox.class);

		registry.put(ToolBarMeta.class, XToolBar.class);		
		registry.put(ButtonMeta.class, XButton.class);
		registry.put(TableMeta.class, XTable.class);
		registry.put(TableColumnMeta.class, XColumn.class);
		registry.put(PanelMeta.class, XPanel.class);
		registry.put(SplitPanelMeta.class, XSplitPanel.class);
		registry.put(TabPanelMeta.class, XTabPanel.class);
		registry.put(PanelMeta.class, XPanel.class);


	}
	
	@SuppressWarnings("rawtypes")
	public static  Class<IRenderer> getRenderer(Class classMeta){
		return (Class<IRenderer>)registry.get(classMeta);
	}
	
	public static  void registry(Class metaClass,Class compClass){
		registry.put(metaClass, compClass);
	}
}