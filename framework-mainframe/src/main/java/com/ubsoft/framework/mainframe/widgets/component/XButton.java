package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.metadata.model.widget.ButtonMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XButton extends JButton implements IRenderer {

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		ButtonMeta compMeta = (ButtonMeta) meta;
		this.meta = compMeta;
		if (compMeta.getLabel() != null) {
			this.setText(compMeta.getLabel());
			this.setToolTipText(compMeta.getLabel());
		}
		if (compMeta.getIcon() != null) {
			setIcon(IconFactory.getImageIcon(compMeta.getIcon()));
		}
		if ((compMeta.getBorder() != null && compMeta.getBorder()==false)||parent instanceof JToolBar) {
			EmptyBorder emptyButtonBorder = new EmptyBorder(new Insets(3, 3, 3, 3));
			this.setBorder(emptyButtonBorder);
		}
		
		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}

	ButtonMeta meta = null;

	@Override
	public ButtonMeta getMeta() {
		return meta;
	}
}
