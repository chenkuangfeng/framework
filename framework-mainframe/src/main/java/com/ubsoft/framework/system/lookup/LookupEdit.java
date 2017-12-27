package com.ubsoft.framework.system.lookup;

import com.ubsoft.framework.mainframe.formbase.EditForm;


public class LookupEdit extends EditForm{	

	public LookupEdit(String entityId) {
		super(entityId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());		
	}	

}
