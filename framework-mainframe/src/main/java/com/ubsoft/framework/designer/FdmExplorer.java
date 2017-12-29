package com.ubsoft.framework.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.core.support.xml.XmlUtil;
import com.ubsoft.framework.mainframe.formbase.ExplorerForm;
import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.component.XTextArea;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public class FdmExplorer extends ExplorerForm {
	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());
	}

	protected void afterRenderer() {
		XButton btnFormat = (XButton) this.getComponent("btnFormat");
		btnFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XTextArea xmlField = (XTextArea) getComponent("txtFdmXml");
				try {
					if (StringUtil.isNotEmpty(xmlField.getText())) {
						String xml = XmlUtil.formatXml(xmlField.getText());
						xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
						xml = xml.replaceFirst("\n", "");
						xml = xml.replaceFirst("\n", "");
						xmlField.setText(xml);
					}
				} catch (Exception ex) {
					MessageBox.showException(ex);
				}
			}
		});
	}
}
