package com.ubsoft.framework.mainframe.widgets.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import com.borland.dbswing.JdbComboBox;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.mainframe.widgets.component.combobox.ValueText;
import com.ubsoft.framework.mainframe.widgets.component.combobox.XComboBoxRenderer;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
import com.ubsoft.framework.system.entity.LookupDetail;
import com.ubsoft.framework.system.service.ILookupDetailService;

public class LookupManager {
	private static Map<String, List<LookupDetail>> lookupCache = new HashMap<String, List<LookupDetail>>();

	public static DataSet getLookupDataSet(String lkKey) throws Exception {
		List<LookupDetail> beans = null;
		if (lookupCache.containsKey(lkKey)) {
			beans = lookupCache.get(lkKey);
		} else {
			ILookupDetailService lkdService = RpcProxy.getProxy(ILookupDetailService.class);
			beans = lkdService.gets(new String[]{"lkKey"},    new Object[]{lkKey} );
			lookupCache.put(lkKey, beans);
		}
		StorageDataSet dataSet = new StorageDataSet();
		Column value = new Column();
		value.setColumnName("value");
		value.setDataType(Variant.STRING);
		Column text = new Column();
		text.setColumnName("text");
		text.setDataType(Variant.STRING);		
		dataSet.setColumns(new Column[] { value, text });
		dataSet.open();
		//DataSetUtil.loadMetaFromBeans(beans, dataSet);
		DataRow blankRow = new DataRow(dataSet);
		blankRow.setString("value",null);
		blankRow.setString("text", null);	
		dataSet.addRow(blankRow);
		for(LookupDetail detail:beans){
			DataRow row = new DataRow(dataSet);
			row.setString("value", detail.getLkdKey());
			row.setString("text", detail.getLkdName());
			dataSet.addRow(row);
		}
		//dataSet.close();
		return dataSet;
	}
	public static void bindComboxBox(JdbComboBox cmb, String lkKey) {
		List<LookupDetail> beans = null;
		if (lookupCache.containsKey(lkKey)) {
			beans = lookupCache.get(lkKey);
		} else {
			ILookupDetailService lkdService = RpcProxy.getProxy(ILookupDetailService.class);
			beans = lkdService.gets(new String[]{"lkKey"},    new Object[]{lkKey} );
			lookupCache.put(lkKey, beans);
		}
		if (beans != null) {
			Vector<ValueText> vector= new Vector<ValueText>();			
			ValueText vtBlank= new ValueText();
			vector.add(vtBlank);
			for(LookupDetail detail:beans){
				ValueText vt= new ValueText();
				vt.setValue(detail.getLkdKey());
				vt.setText(detail.getLkdName());
				vector.add(vt);
			}
			cmb.setModel(new DefaultComboBoxModel(vector));
			cmb.setRenderer(new XComboBoxRenderer());
			cmb.setEditable(false);			
		}
	}
}
