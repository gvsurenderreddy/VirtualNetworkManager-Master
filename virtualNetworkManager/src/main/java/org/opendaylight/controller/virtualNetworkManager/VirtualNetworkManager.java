/*
 * Copyright (C) 2014 SDN Hub

 Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.
 You may not use this file except in compliance with this License.
 You may obtain a copy of the License at

    http://www.gnu.org/licenses/gpl-3.0.txt

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied.

 *
 */

package org.opendaylight.controller.virtualNetworkManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.lang.String;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.opendaylight.controller.protocol_plugin.openflow13.core.IController;
import org.opendaylight.controller.protocol_plugin.openflow13.core.IMessageListener;
import org.opendaylight.controller.protocol_plugin.openflow13.core.ISwitch;
import org.opendaylight.controller.protocol_plugin.openflow13.core.ISwitchStateListener;
import org.opendaylight.controller.sal.core.ConstructionException;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.packet.BitBufferHelper;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.action.Action;
import org.opendaylight.controller.sal.action.Output;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.match.MatchType;
import org.opendaylight.controller.sal.match.MatchField;
import org.opendaylight.controller.sal.utils.Status;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.openflow.protocol.OFType;

public class VirtualNetworkManager implements IListenDataPacket, IController{
	
    private static final Logger logger = LoggerFactory
            .getLogger(VirtualNetworkManager.class);
    private ISwitchManager switchManager = null;
    private IFlowProgrammerService flowProgrammer = null;
    private IDataPacketService dataPacketService = null;
    private Map<Long, NodeConnector> mac_to_port = new HashMap<Long, NodeConnector>();
    private ISwitchStateListener switchStateListner = null;
    private String function = "switch";

    void setDataPacketService(IDataPacketService s) {
        this.dataPacketService = s;
    }

    void unsetDataPacketService(IDataPacketService s) {
        if (this.dataPacketService == s) {
            this.dataPacketService = null;
        }
    }

    public void setFlowProgrammerService(IFlowProgrammerService s)
    {
        this.flowProgrammer = s;
    }

    public void unsetFlowProgrammerService(IFlowProgrammerService s) {
        if (this.flowProgrammer == s) {
            this.flowProgrammer = null;
        }
    }

    void setSwitchManager(ISwitchManager s) {
        logger.debug("SwitchManager set");
        this.switchManager = s;
    }

    void unsetSwitchManager(ISwitchManager s) {
        if (this.switchManager == s) {
            logger.debug("SwitchManager removed!");
            this.switchManager = null;
        }
    }

    /**
     * Function called by the dependency manager when all the required
     * dependencies are satisfied
     *
     */
    void init() {
        logger.info("Initialized");
        // Disabling the SimpleForwarding and ARPHandler bundle to not conflict with this one
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        for(Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getSymbolicName().contains("simpleforwarding")) {
                try {
                    bundle.uninstall();
                } catch (BundleException e) {
                    logger.error("Exception in Bundle uninstall "+bundle.getSymbolicName(), e); 
                }   
            }   
        }   
 
    }

    /**
     * Function called by the dependency manager when at least one
     * dependency become unsatisfied or when the component is shutting
     * down because for example bundle is being stopped.
     *
     */
    void destroy() {
    }

    /**
     * Function called by dependency manager after "init ()" is called
     * and after the services provided by the class are registered in
     * the service registry
     *
     */
    void start() {
    	SwitchStateManager switchStateManager = null;
    	OFEventManager ofEventManager = null;
    
        logger.info("Initializing Switch State Manager");
        switchStateManager = new SwitchStateManager();
        switchStateManager.setDataPacketService(dataPacketService);
        switchStateManager.setFlowProgrammerService(flowProgrammer);
        switchStateManager.setSwitchManager(switchManager);
        addSwitchStateListener(switchStateManager);
        
        logger.info("Initializing OF Event Manager ");
        ofEventManager = new OFEventManager();
        ofEventManager.setDataPacketService(dataPacketService);
        ofEventManager.setFlowProgrammerService(flowProgrammer);
        ofEventManager.setSwitchManager(switchManager);
        ofEventManager.registerHandler(this);
    }

    /**
     * Function called by the dependency manager before the services
     * exported by the component are unregistered, this will be
     * followed by a "destroy ()" calls
     *
     */
    void stop() {
        logger.info("Stopped");
    }

    private void floodPacket(RawPacket inPkt) {
        NodeConnector incoming_connector = inPkt.getIncomingNodeConnector();
        Node incoming_node = incoming_connector.getNode();

        Set<NodeConnector> nodeConnectors =
                this.switchManager.getUpNodeConnectors(incoming_node);

        for (NodeConnector p : nodeConnectors) {
            if (!p.equals(incoming_connector)) {
                try {
                    RawPacket destPkt = new RawPacket(inPkt);
                    destPkt.setOutgoingNodeConnector(p);
                    this.dataPacketService.transmitDataPacket(destPkt);
                } catch (ConstructionException e2) {
                    continue;
                }
            }
        }
    }



    @Override
    public PacketResult receiveDataPacket(RawPacket inPkt) {
 
    	logger.info("<<<<<<>>>>>>>  Packet Received: " + inPkt);
    	
    	if (inPkt == null) {
    		logger.info("<<<<<<>>>>>>>  Packet Received: Null ");
            return PacketResult.IGNORED;
        }

        NodeConnector incoming_connector = inPkt.getIncomingNodeConnector();

        // Hub implementation
        logger.info("<<<<<<>>>>>>> State of Switch: " + function);
        if (function.equals("hub")) {
        	floodPacket(inPkt);
        } else {
            Packet formattedPak = this.dataPacketService.decodeDataPacket(inPkt);
            if (!(formattedPak instanceof Ethernet)) {
                return PacketResult.IGNORED;
            }

            logger.info("learning Source MAC: setting incoming connector for the source MAC");
            learnSourceMAC(formattedPak, incoming_connector);
            NodeConnector outgoing_connector = 
                knowDestinationMAC(formattedPak);
            if (outgoing_connector == null) {
            	logger.info("<<<<<<>>>>>>>  No outgoing connector for the MAC: Flooding packet: " + inPkt);
                floodPacket(inPkt);
            } else {
            	logger.info("<<<<<<>>>>>>>  Outgoing connector Found for the MAC: Set flow and transmit: " + inPkt);
                if (!programFlow(formattedPak, incoming_connector,
                            outgoing_connector)) {
                    return PacketResult.IGNORED;
                }
                inPkt.setOutgoingNodeConnector(outgoing_connector);
                this.dataPacketService.transmitDataPacket(inPkt);
            }
        }
        return PacketResult.CONSUME;
    }

    private void learnSourceMAC(Packet formattedPak, NodeConnector incoming_connector) {
        byte[] srcMAC = ((Ethernet)formattedPak).getSourceMACAddress();
        long srcMAC_val = BitBufferHelper.toNumber(srcMAC);
        this.mac_to_port.put(srcMAC_val, incoming_connector);
    }

    private NodeConnector knowDestinationMAC(Packet formattedPak) {
        byte[] dstMAC = ((Ethernet)formattedPak).getDestinationMACAddress();
        long dstMAC_val = BitBufferHelper.toNumber(dstMAC);
        return this.mac_to_port.get(dstMAC_val) ;
    }

    private boolean programFlow(Packet formattedPak, 
            NodeConnector incoming_connector, 
            NodeConnector outgoing_connector) {
        byte[] dstMAC = ((Ethernet)formattedPak).getDestinationMACAddress();

        Match match = new Match();
        match.setField( new MatchField(MatchType.IN_PORT, incoming_connector) );
        match.setField( new MatchField(MatchType.DL_DST, dstMAC.clone()) );

        List<Action> actions = new ArrayList<Action>();
        actions.add(new Output(outgoing_connector));

        Flow f = new Flow(match, actions);
        f.setIdleTimeout((short)5);

        // Modify the flow on the network node
        Node incoming_node = incoming_connector.getNode();
        Status status = flowProgrammer.addFlow(incoming_node, f);

        if (!status.isSuccess()) {
            logger.warn("SDN Plugin failed to program the flow: {}. The failure is: {}",
                    f, status.getDescription());
            return false;
        } else {
            return true;
        }
    }

	@Override
	public void addMessageListener(OFType arg0, IMessageListener arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSwitchStateListener(ISwitchStateListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ISwitch getSwitch(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, ISwitch> getSwitches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeMessageListener(OFType arg0, IMessageListener arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSwitchStateListener(ISwitchStateListener arg0) {
		// TODO Auto-generated method stub
		
	}
}
