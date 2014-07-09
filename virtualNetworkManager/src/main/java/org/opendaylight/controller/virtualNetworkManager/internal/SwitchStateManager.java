package org.opendaylight.controller.virtualNetworkManager.internal;

import org.opendaylight.controller.protocol_plugin.openflow.core.ISwitch;
import org.opendaylight.controller.protocol_plugin.openflow.core.ISwitchStateListener;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchStateManager implements ISwitchStateListener {

	private static final Logger logger = LoggerFactory
            .getLogger(SwitchStateManager.class);
	private VnmServicePojo services = null;
	
	public VnmServicePojo getServices() {
		return services;
	}

	public void setServices(VnmServicePojo services) {
		this.services = services;
	}

	@Override
	public void switchAdded(ISwitch arg0) {
		// TODO Auto-generated method stub
		logger.info("New OpenFlow Switch Added Info has reached to VNM!");
	}

	@Override
	public void switchDeleted(ISwitch arg0) {
		// TODO Auto-generated method stub
		logger.info("New OpenFlow Switch Deleted Info has reached to VNM!");
	}

	
}
