package org.opendaylight.controller.virtualNetworkManager.internal;

import org.opendaylight.controller.virtualNetworkManager.openflowMessageHandler.*;
import org.openflow.protocol.OFType;


public class OFEventManager {

	private VnmServicePojo services = null;
	
	public VnmServicePojo getServices() {
		return services;
	}

	public void setServices(VnmServicePojo services) {
		this.services = services;
	}

	public void registerMessageHandlers(){
		
		BarrierReplyHandler barrierHandler = new BarrierReplyHandler();
		barrierHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.BARRIER_REPLY, barrierHandler);
		
		ConfigReplyHandler configHandler = new ConfigReplyHandler();
		configHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.GET_CONFIG_REPLY, configHandler);
		
		FeaturesReplyHandler featuresHandler = new FeaturesReplyHandler();
		featuresHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.FEATURES_REPLY, featuresHandler);
		
		FlowModHandler flowModHandler = new FlowModHandler();
		flowModHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.FLOW_MOD, flowModHandler);
		
		FlowRemovedHandler flowRemovedHandler = new FlowRemovedHandler();
		flowRemovedHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.FLOW_REMOVED, flowRemovedHandler);
		
		PacketInHandler pktInHandler = new PacketInHandler();
		pktInHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.PACKET_IN, pktInHandler);
		
		PortModHandler portModHandler = new PortModHandler();
		portModHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.PORT_MOD, portModHandler);
		
		PortStatusHandler portStatusHandler = new PortStatusHandler();
		portStatusHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.PORT_STATUS, portStatusHandler);
		
		StatsReplyHandler statsReplyHandler = new StatsReplyHandler();
		statsReplyHandler.setServices(services);
		services.getControllerService().addMessageListener(OFType.STATS_REPLY, statsReplyHandler);	
		
	}
	
}
