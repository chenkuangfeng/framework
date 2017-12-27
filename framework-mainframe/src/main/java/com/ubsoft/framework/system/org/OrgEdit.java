package com.ubsoft.framework.system.org;

import com.ubsoft.framework.mainframe.formbase.EditForm;


public class OrgEdit extends EditForm{	

	public OrgEdit(String entityId) {
		super(entityId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());	
	}	

}
