package com.ubsoft.framework.mainframe.formbase;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.Map;

import javax.swing.JInternalFrame;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Transient;
import com.ubsoft.framework.core.service.IFormService;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.widgets.component.BusyPanel;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
import com.ubsoft.framework.system.MainFrame;

public abstract class Form extends JInternalFrame {

	protected String ACTION_NEW = "new";
	protected String ACTION_SAVE = "save";
	protected String ACTION_DELETE = "delete";
	protected String ACTION_REFRESH = "refresh";
	protected String ACTION_CLOSE = "close";
	protected BusyPanel busyPanel;

	protected IFormService formService = RpcProxy.getProxy(IFormService.class);
	protected FormEngine formEngine = new FormEngine();

	public MainFrame getMainFrame() {
		return MainFrame.getMainFrame();
	}

	public void setStatusMessage(String statusMessage) {
		this.getMainFrame().getStatusBar().setMessage(statusMessage);
	}

	protected Component getComponent(String id) {
		return formEngine.getComponent(id);
	}

	protected void setBusy(boolean busy) {
		if (busy) {
			busyPanel.stop();
			busyPanel.start();
			busyPanel.setVisible(true);
		} else {
			busyPanel.stop();
			busyPanel.setVisible(false);
		}
	}

	protected void initPrivateControl(Class clazz, Object obj) {

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			boolean fieldHasAnno = field.isAnnotationPresent(Control.class);
			String fieldName = field.getName();
			if (fieldHasAnno) {
				Control column = field.getAnnotation(Control.class);
				field.setAccessible(true);
				try {
					field.set(obj, getComponent(field.getName()));
				} catch (Exception e) {
					MessageBox.showException(e);

				}
				field.setAccessible(false);

			}

		}

	}

	/**
	 * 界面初始化入口
	 * 
	 * @param formAction
	 */
	public abstract void initForm();

	// /**component
	// * 执行form动作之前调用
	// *
	// * @param formAction
	// * :[save:保存动作;del：删除动作;new:新增动作;....自定义动作]
	// */
	// protected abstract boolean beforePerformed(String formAction);
	//
	// /**
	// * 执行form动作之后调用
	// *
	// * @param formAction
	// * :[save:保存动作;del：删除动作;new:新增动作;....自定义动作]
	// */
	// protected abstract void afterPerformed(String formAction);

}
