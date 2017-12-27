package com.ubsoft.framework.system.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.List;

import javax.swing.ListSelectionModel;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.ConditionNode;
import com.ubsoft.framework.mainframe.formbase.SelectForm;

public class SelectDialog extends SelectForm {
	public List<ConditionNode> conditionNode;
	public SelectDialog(String formId,Dialog owner) {
		super(formId,owner);
		
	}
	public SelectDialog(String formId) {
		super(formId);
		
	}
	public SelectDialog(String formId,Frame owner) {
		super(formId,owner);
		
	}
	public static List<Bio> select(String formId, Component parent, List<ConditionNode> filter, boolean multiSelect) {
		SelectDialog dialog;
		if (parent == null) {
			dialog = new SelectDialog(formId);
		} else if (parent instanceof Dialog) {
			dialog = new SelectDialog(formId,(Dialog) parent);
		} else if (parent instanceof Frame) {
			dialog = new SelectDialog(formId,(Frame) parent);
		} else {
			Container window = parent.getParent();
			while (window != null) {
				if (window instanceof Dialog || window instanceof Frame) {
					break;
				}
				window = window.getParent();
			}
			if (window != null) {
				if (window instanceof Dialog) {
					dialog = new SelectDialog(formId,(Dialog) window);
				} else {
					dialog = new SelectDialog(formId,(Frame) window);
				}
			} else {
				dialog = new SelectDialog(formId);
			}
		}		
		dialog.conditionNode=filter;
		dialog.setLocationRelativeTo(dialog.getParent());
		dialog.setModal(true);
		if (multiSelect) {
			dialog.mainTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			dialog.mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		
		dialog.setVisible(true);
		return dialog.result;
	}
	//加入默认查询条件
	@Override
	public void addDefaultQueryCondition(List<ConditionNode> nodes){
		if(conditionNode!=null){
			for(ConditionNode node:conditionNode){
				nodes.add(node);
			}
		}
	}

}
