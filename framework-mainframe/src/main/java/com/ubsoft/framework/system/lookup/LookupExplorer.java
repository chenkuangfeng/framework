package com.ubsoft.framework.system.lookup;

import com.ubsoft.framework.mainframe.formbase.ExplorerForm;

public class LookupExplorer extends ExplorerForm {
	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());
	}

	protected void afterLoad() {
		
	}
}
