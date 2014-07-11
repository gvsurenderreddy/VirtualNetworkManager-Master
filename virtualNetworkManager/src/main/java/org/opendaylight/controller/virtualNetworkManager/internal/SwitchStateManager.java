package org.opendaylight.controller.virtualNetworkManager.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchStateManager{

	private static final Logger logger = LoggerFactory
            .getLogger(SwitchStateManager.class);
	private VnmServicePojo services = null;

	public VnmServicePojo getServices() {
		return services;
	}

	public void setServices(VnmServicePojo services) {
		this.services = services;
	}

	public void switchAdded() {
		// TODO Auto-generated method stub
		logger.info("New OpenFlow Switch Added Info has reached to VNM!");
	}

	public void switchDeleted() {
		// TODO Auto-generated method stub
		logger.info("New OpenFlow Switch Deleted Info has reached to VNM!");
	}


}
