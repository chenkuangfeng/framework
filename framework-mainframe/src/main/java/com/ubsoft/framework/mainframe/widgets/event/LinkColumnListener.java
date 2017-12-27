package com.ubsoft.framework.mainframe.widgets.event;

import java.util.EventListener;

public  interface LinkColumnListener extends EventListener
{
   void link(Object source, Object result);
}