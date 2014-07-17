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

package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.Map;

import org.opendaylight.controller.forwardingrulesmanager.IForwardingRulesManager;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.UpdateType;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.IInventoryListener;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.virtualNetworkManager.IVirtualNetworkManager;
import org.opendaylight.controller.virtualNetworkManager.objectStore.SliceTree;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualNetworkManagerImpl implements IVirtualNetworkManager, IInventoryListener, IListenDataPacket{

	/* Internal Global */
	int thread_no = 0;
	private static final Logger logger = LoggerFactory
            .getLogger(VirtualNetworkManagerImpl.class);

	/* External services */
	private ISwitchManager switchManager = null;
    private IFlowProgrammerService flowProgrammer = null;
    private IDataPacketService dataPacketService = null;
    private IForwardingRulesManager forwardingRulesManager = null;
	private IStatisticsManager statManager = null;

	/* Internal Project Globals */
	private VnmServicePojo services = null;
	private SliceTree sliceTree = null;
	private TopologyTree topoTree = null;

	/* Internal Modules */
	private SwitchEventManager switchEventManager = null;
	private SliceManager sliceManager = null;
	private FlowManager flowManager = null;
	private OverlayNetworkManager overlayNetworkManager = null;



	/* Default Constructor */
	public VirtualNetworkManagerImpl() {
		super();
		logger.info("Vnm getting instancetiated");
	}

	/* Setter and UnSetter of External Services */
	void setDataPacketService(IDataPacketService s) {
    	logger.info("Datapacketservice set");
        this.dataPacketService = s;
    }

    void unsetDataPacketService(IDataPacketService s) {
    	logger.info("Datapacketservice reset");
        if (this.dataPacketService == s) {
            this.dataPacketService = null;
        }
    }

    public void setFlowProgrammerService(IFlowProgrammerService s)
    {
    	logger.info("FlowProgrammer is set!");
        this.flowProgrammer = s;
    }

    public void unsetFlowProgrammerService(IFlowProgrammerService s) {
    	logger.info("FlowProgrammer is removed!");
        if (this.flowProgrammer == s) {
            this.flowProgrammer = null;
        }
    }

    void setSwitchManager(ISwitchManager s) {
        logger.info("SwitchManager is set!");
        this.switchManager = s;
    }

    void unsetSwitchManager(ISwitchManager s) {
        if (this.switchManager == s) {
            logger.info("SwitchManager is removed!");
            this.switchManager = null;
        }
    }

    void setForwardingRulesManager(IForwardingRulesManager s){
    	logger.info("ForwardingRulesManager is set!");
    	forwardingRulesManager = s;
    }

    void unsetForwardingRulesManager(IForwardingRulesManager s){
    	if (this.forwardingRulesManager == s) {
            logger.info("Controller is removed!");
            this.forwardingRulesManager = null;
        }
    }

    void setStatisticsManager(IStatisticsManager s){
    	logger.info("Statistics Manager is set!");
    	statManager = s;
    }

    void unsetStatisticsManager(IStatisticsManager s){
    	if (this.statManager  == s) {
            logger.info("Statistics Manager is removed!");
            this.statManager = null;
        }
    }


    /* Function to be called by ODL */

    /**
     * Function called by the dependency manager when all the required
     * dependencies are satisfied
     *
     */
    void init() {
    	logger.info("Vnm getting Initilizing by Dependency Manager!");

    	logger.info("Initializing Services Pojo!");
    	services = new VnmServicePojo();
    	services.setDataPacketService(dataPacketService);
    	services.setFlowProgrammer(flowProgrammer);
    	services.setSwitchManager(switchManager);

    	logger.info("Initializing Slice Tree!");
    	sliceTree = SliceTree.init();

    	logger.info("Initializing Topology Tree!");
    	topoTree = TopologyTree.init();

    	logger.info("Initializing Switch Event Manager!");
    	switchEventManager = new SwitchEventManager();
    	switchEventManager.setServices(services);
    	switchEventManager.setTopoTree(topoTree);

    	logger.info("Initializing Flow Manager!");
    	flowManager = new FlowManager();
    	flowManager.setServices(services);
    	flowManager.setTopoTree(topoTree);

    	logger.info("Initializing Overlay Network Manager!");
    	overlayNetworkManager = new OverlayNetworkManager();

    	logger.info("Initializing Slice Manager!");
    	sliceManager = new SliceManager();
    	sliceManager.setServices(services);
    	sliceManager.setSliceTree(sliceTree);
    	sliceManager.setTopoTree(topoTree);
    	/*
    	sliceManager.setFlowManager(flowManager);
    	sliceManager.setOverLayManager()
    	 */

    }

    /**
     * Function called by the dependency manager when at least one
     * dependency become unsatisfied or when the component is shutting
     * down because for example bundle is being stopped.
     *
     */
    void destroy() {
        logger.error("Virtual Network Manager is destroyed!");
    }

    /**
     * Function called by dependency manager after "init ()" is called
     * and after the services provided by the class are registered in
     * the service registry
     *
     */
    void start() {
    	logger.info("Virtual Network Manager is started!");


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



    /* InventoryListener service Interface - internal use only, not exposed */

	@Override
	public void notifyNode(Node node, UpdateType type, Map<String, Property> propMap) {
		// TODO Auto-generated method stub
		//logger.info("notifyNode: Type " + type);

        if (node == null) {
            logger.warn("New Node Notification : Node is null ");
            return;
        }
        /* We only support OpenFlow switches for now */
        if (node.getType().equals(Node.NodeIDType.OPENFLOW)) {
            logger.info("OpenFlow node {} notification", node);
            if(switchEventManager != null){
            	switchEventManager.switchChanged(node, type, propMap);
            	return;
            }
            else {
            	logger.error("switchStateManager is not initialised - should be initialised to process node notification!");
            }
        }
	}

	@Override
	public void notifyNodeConnector(NodeConnector nodeConnector, UpdateType type, Map<String, Property> propMap) {
		// TODO Auto-generated method stub
		//logger.info("notifyNodeConnector: Type " + type);
        if (nodeConnector == null) {
            logger.warn("New NodeConnector Notification : NodeConnector is null");
            return;
        }
        logger.warn("New NodeConnector Notification : {}",nodeConnector);
        switchEventManager.portChanged(nodeConnector, type);
	}



	/* IListenDataPacket services Interface - internal use only, not exposed */

	@Override
	public PacketResult receiveDataPacket(RawPacket pkt) {
		// TODO Auto-generated method stub
		Packet formattedPacket = null;
		Match packetMatch = null;
		logger.warn("New Unexpected Data Packet Received by VNM !");
		logger.info("Packet Details: ");
		formattedPacket = this.dataPacketService.decodeDataPacket(pkt);
		// http://archive.openflow.org/wk/?title=OpenDayLight_Tutorial
		return null;
	}



	/* VirtualNetworkManager service Interface - exposed as a service */

	@Override
	public void testVnm() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int i = 0;
		this.thread_no += 1;
		while(i < 50){
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("Sleep error");
			}
			logger.info("I'm thread " + this.thread_no + " printing : " + i);
			i++;
		}
	}

	@Override
	public void addSlice(int sliceId, String desc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSwitchToSlice(int sliceId, String dataPathId, String name,
			String port, String desc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPortToSwitch(String dataPathId, String MAC, String desc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerAgentToSwitch(String dataPathId, String agentUri) {
		// TODO Auto-generated method stub

	}

}
