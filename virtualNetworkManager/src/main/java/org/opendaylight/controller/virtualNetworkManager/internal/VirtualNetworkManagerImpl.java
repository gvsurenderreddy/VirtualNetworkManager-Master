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
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.IInventoryListener;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.virtualNetworkManager.core.IVirtualNetworkManager;
import org.opendaylight.controller.virtualNetworkManager.core.VNMPort;
import org.opendaylight.controller.virtualNetworkManager.core.VNMSwitch;
import org.opendaylight.controller.virtualNetworkManager.core.VNMTunnel;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualNetworkManagerImpl implements IVirtualNetworkManager, IInventoryListener, IListenDataPacket{

	private static final Logger logger = LoggerFactory
            .getLogger(VirtualNetworkManagerImpl.class);
    private ISwitchManager switchManager = null;
    private IFlowProgrammerService flowProgrammer = null;
    private IDataPacketService dataPacketService = null;
    private IForwardingRulesManager forwardingRulesManager = null;
	private IStatisticsManager statManager = null;
	private SwitchStateManager switchStateManager = null;

	public VirtualNetworkManagerImpl() {
		super();
		logger.info("Vnm getting instancetiated");
	}

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

    /**
     * Function called by the dependency manager when all the required
     * dependencies are satisfied
     *
     */
    void init() {
    	logger.info("Vnm getting Initilizing by Dependency Manager!");
        // Disabling the SimpleForwarding and ARPHandler bundle to not conflict with this one
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        for(Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getSymbolicName().contains("simpleforwarding")) {
                try {
                	logger.info("Uninstalling Bundle:'simpleforwarding' to avoid conflict!");
                    bundle.uninstall();
                } catch (BundleException e) {
                    logger.warn("Exception in Bundle uninstall: "+bundle.getSymbolicName(), e);
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
        logger.error("Virtual Network Manager is destroyed!");
    }

    /**
     * Function called by dependency manager after "init ()" is called
     * and after the services provided by the class are registered in
     * the service registry
     *
     */
    void start() {

        VnmServicePojo services = null;
        logger.info("Virtual Network Manager is started!");

    	logger.info("Initializing Services Pojo!");
    	services = new VnmServicePojo();
    	services.setDataPacketService(dataPacketService);
    	services.setFlowProgrammer(flowProgrammer);
    	services.setSwitchManager(switchManager);

    	switchStateManager = new SwitchStateManager();
    	switchStateManager.setServices(services);
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
            if(switchStateManager != null){
            	switchStateManager.switchChanged(node, type, propMap);
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
        Node node = nodeConnector.getNode();
        //this.logNodeInfo(node, propMap);

        switch (type) {
        case ADDED:
           logger.info("NodeConnector {} for node {} added ", nodeConnector, node);
           break;

        case CHANGED:
            logger.info("NodeConnector {} for node {} changed ", nodeConnector, node);
            break;

        case REMOVED:
            logger.info("NodeConnector {} for node {} removed", nodeConnector, node);
            break;

        default:
            logger.info("Unknown NodeConnector type received : " + type);
            break;
        }
	}

	/* IListenDataPacket services Interface - internal use only, not exposed */

	@Override
	public PacketResult receiveDataPacket(RawPacket pkt) {
		// TODO Auto-generated method stub
		logger.warn("New Unexpected Data Packet Received by VNM !");
		logger.info("Packet Details: " + pkt.toString());
		return null;
	}

	/* VirtualNetworkManager service Interface - exposed as a service */

	@Override
	public void addSwitch(VNMSwitch vswitch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSWitch(VNMSwitch vnode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSwitch(VNMSwitch vswitch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNode(VNMPort vnode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNode(VNMPort vnode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteNode(VNMPort vnode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTunnel(VNMSwitch vswitch, VNMTunnel vtunnel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTunnel(VNMSwitch vswitch, VNMTunnel vtunnel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTunnel(VNMTunnel vtunnel) {
		// TODO Auto-generated method stub

	}



}
