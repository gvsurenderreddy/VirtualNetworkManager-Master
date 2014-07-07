package org.opendaylight.controller.virtualNetworkManager.OFMessageHandler;

import org.opendaylight.controller.protocol_plugin.openflow13.core.IMessageListener;
import org.opendaylight.controller.protocol_plugin.openflow13.core.ISwitch;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.openflow.protocol.OFMessage;

public class FlowModHandler implements IMessageListener {

	private IFlowProgrammerService programmer = null;
	private IDataPacketService dataPacketService = null;
	private ISwitchManager switchManager = null;

	@Override
	public void receive(ISwitch ofSwitch, OFMessage message) {
		// TODO Auto-generated method stub

	}

	public void setFlowProgrammerService(IFlowProgrammerService s){
        this.programmer = s;
    }
	
	void setDataPacketService(IDataPacketService s) {
        this.dataPacketService = s;
    }
	
	void setSwitchManager(ISwitchManager s) {
        this.switchManager = s;
    }

}
