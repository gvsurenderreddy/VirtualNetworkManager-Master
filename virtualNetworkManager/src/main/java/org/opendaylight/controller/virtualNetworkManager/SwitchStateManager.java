package org.opendaylight.controller.virtualNetworkManager;

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
	private IFlowProgrammerService programmer = null;
	private IDataPacketService dataPacketService = null;
	private ISwitchManager switchManager = null;

	public void setFlowProgrammerService(IFlowProgrammerService s){
        this.programmer = s;
    }
	
	void setDataPacketService(IDataPacketService s) {
        this.dataPacketService = s;
    }
	
	void setSwitchManager(ISwitchManager s) {
        this.switchManager = s;
    }

	@Override
	public void switchAdded(ISwitch arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void switchDeleted(ISwitch arg0) {
		// TODO Auto-generated method stub
		
	}
}
