package com.ubsoft.framework.mainframe.widgets.event;

import java.util.EventListener;
import java.util.List;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.mainframe.widgets.component.XLookupField;

public  interface LookupListener extends EventListener
{
   void lookup(XLookupField source, List<Bio> result);
}