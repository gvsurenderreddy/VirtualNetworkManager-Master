package org.opendaylight.controller.virtualNetworkManager.internal;

import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.switchmanager.ISwitchManager;

public class VnmServicePojo {
	private IFlowProgrammerService flowProgrammer = null;
	private IDataPacketService dataPacketService = null;
	private ISwitchManager switchManager = null;


	public ISwitchManager getSwitchManager() {
		return switchManager;
	}
	public void setSwitchManager(ISwitchManager switchManager) {
		this.switchManager = switchManager;
	}
	public IDataPacketService getDataPacketService() {
		return dataPacketService;
	}
	public void setDataPacketService(IDataPacketService dataPacketService) {
		this.dataPacketService = dataPacketService;
	}

	public IFlowProgrammerService getFlowProgrammer() {
		return flowProgrammer;
	}
	public void setFlowProgrammer(IFlowProgrammerService flowProgrammer) {
		this.flowProgrammer = flowProgrammer;
	}
}
