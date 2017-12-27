package com.ubsoft.framework.mainframe.widgets.event;

import java.util.EventListener;
import java.util.List;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;

public  interface LookupColumnListener extends EventListener
{
   void lookup(XColumn source, List<Bio> result);
}