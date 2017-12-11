package com.ubsoft.framework.esb.process.impl;

import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.process.IProcess;

public abstract class ProcessImpl implements IProcess {
	public abstract void process(Exchange ex) ;
}
