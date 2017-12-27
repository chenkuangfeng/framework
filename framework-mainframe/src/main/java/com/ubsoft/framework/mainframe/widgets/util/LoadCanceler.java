package com.ubsoft.framework.mainframe.widgets.util;

import com.borland.dx.dataset.LoadCancel;

public abstract interface LoadCanceler extends LoadCancel
{
  public abstract boolean isCancelled();

  public abstract void clearCancelled();
}