package com.ubsoft.framework.esb.model;

import com.ubsoft.framework.esb.entity.Endpoint;

public class Exchange {

	private Message in;
	private Message out;
	public Message getIn() {
		return in;
	}
	public void setIn(Message in) {
		this.in = in;
	}
	public Message getOut() {
		return out;
	}
	public void setOut(Message out) {
		this.out = out;
	}
	private Endpoint endpoint;
	public Endpoint getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
}
