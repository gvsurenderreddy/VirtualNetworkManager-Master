package org.opendaylight.controller.virtualNetworkManager.internal;

import org.opendaylight.controller.protocol_plugin.openflow.core.IController;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.switchmanager.ISwitchManager;

public class VnmServicePojo {
	private IFlowProgrammerService flowProgrammer = null;
	private IDataPacketService dataPacketService = null;
	private ISwitchManager switchManager = null;
	private IController controllerService = null;
	
	
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
	public IController getControllerService() {
		return controllerService;
	}
	public void setControllerService(IController controllerService) {
		this.controllerService = controllerService;
	}
	public IFlowProgrammerService getFlowProgrammer() {
		return flowProgrammer;
	}
	public void setFlowProgrammer(IFlowProgrammerService flowProgrammer) {
		this.flowProgrammer = flowProgrammer;
	}
}
