package com.ubsoft.framework.system.lookup;

import com.ubsoft.framework.mainframe.formbase.ListForm;


public class LookupList extends ListForm{
	@Override
	public void initForm() {
		this.showSaveBtn=false;
		this.showCancelBtn=false;
		renderer(getClass().getSimpleName());
	}	
	

	
	
}
