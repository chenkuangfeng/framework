package com.ubsoft.framework.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.designer.service.IDbTableMetaService;
import com.ubsoft.framework.mainframe.formbase.ExplorerForm;
import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.metadata.model.form.fdm.MasterMeta;
import com.ubsoft.framework.rpc.proxy.RpcProxy;

public class BioExplorer extends ExplorerForm {
	private XButton btnSync;
	IDbTableMetaService dbService = RpcProxy.getProxy(IDbTableMetaService.class);

	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());
	}

	protected void afterRenderer() {
		btnSync = (XButton) this.getComponent("btnSyncBio");
		btnSync.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				syncBioMetaFormTable();

			}
		});
	}

	private void syncBioMetaFormTable() {
		if (mainTable.getSelectedRowCount() > 0) {
			int[] rowIndex = mainTable.getSelectedRows();
			int n = JOptionPane.showConfirmDialog(null, "是否同步" + mainTable.getSelectedRowCount() + "条记录?", "确认同步", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				for (int i = 0; i < rowIndex.length; i++) {
					this.lstDataSet.goToRow(rowIndex[i]);
					final String table = lstDataSet.getString("tableKey");
					setStatusMessage("正在同步" + table);
					dbService.updateBioMetaFromTable("DefaultDS", "", "", new String[] { table });

				}
				setStatusMessage("就绪");
			}
		} else {
			MessageBox.showInfo("请选择要同步的记录.");
		}
	}
}
