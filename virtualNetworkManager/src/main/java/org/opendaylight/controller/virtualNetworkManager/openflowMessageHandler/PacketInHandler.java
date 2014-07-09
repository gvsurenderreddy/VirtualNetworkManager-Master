package org.opendaylight.controller.virtualNetworkManager.openflowMessageHandler;

import org.opendaylight.controller.protocol_plugin.openflow.core.IMessageListener;
import org.opendaylight.controller.protocol_plugin.openflow.core.ISwitch;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.openflow.protocol.OFMessage;

public class PacketInHandler extends OFMessageHandler {

	@Override
	public void receive(ISwitch ofSwitch, OFMessage message) {
		// TODO Auto-generated method stub

	}

}
