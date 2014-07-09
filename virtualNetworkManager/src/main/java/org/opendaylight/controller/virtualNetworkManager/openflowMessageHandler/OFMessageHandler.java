package org.opendaylight.controller.virtualNetworkManager.openflowMessageHandler;

import org.opendaylight.controller.protocol_plugin.openflow.core.IMessageListener;
import org.opendaylight.controller.virtualNetworkManager.internal.VnmServicePojo;

public abstract class OFMessageHandler implements IMessageListener {
	private VnmServicePojo services = null;
	
	public VnmServicePojo getServices() {
		return services;
	}

	public void setServices(VnmServicePojo services) {
		this.services = services;
	}
	
	
}
