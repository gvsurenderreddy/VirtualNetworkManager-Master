
/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Dictionary;
import java.util.Iterator;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.apache.felix.dm.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.protocol_plugin.openflow.core.IController;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.discovery.IDiscoveryService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.topology.ITopologyService;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.opendaylight.controller.virtualNetworkManager.core.IVirtualNetworkManager;


public class Activator extends ComponentActivatorAbstractBase {
    protected static final Logger logger = LoggerFactory
            .getLogger(Activator.class);

    /**
     * Function called when the activator starts just after some
     * initializations are done by the
     * ComponentActivatorAbstractBase.
     *
     */
    public void init() {

    }

    /**
     * Function called when the activator stops just before the
     * cleanup done by ComponentActivatorAbstractBase
     *
     */
    public void destroy() {

    }

    /**
     * Function that is used to communicate to dependency manager the
     * list of known implementations for services inside a container
     *
     *
     * @return An array containing all the CLASS objects that will be
     * instantiated in order to get an fully working implementation
     * Object
     */
    public Object[] getImplementations() {
    	logger.info("Bundle getting Virtual Network Manager implementation info!");
        Object[] res = { VirtualNetworkManagerImpl.class};
        return res;
    }

    /**
     * Function that is called when configuration of the dependencies
     * is required.
     *
     * @param c dependency manager Component object, used for
     * configuring the dependencies exported and imported
     * @param imp Implementation class that is being configured,
     * needed as long as the same routine can configure multiple
     * implementations
     * @param containerName The containerName being configured, this allow
     * also optional per-container different behavior if needed, usually
     * should not be the case though.
     */
    public void configureInstance(Component c, Object imp, String containerName) {
    	
        if (imp.equals(VirtualNetworkManagerImpl.class)) {
            
        	logger.info("Exporting the VNM services as VirtualNetworkManager");      	
        	
        	Dictionary<String, String> props = new Hashtable<String, String>();
        	props.put("salListenerName", "VirtualNetworkManager");
        	c.setInterface(new String[] { IVirtualNetworkManager.class.getName() }, props);
            
        	
            logger.info("Registering dependent services");
                
            c.add(createContainerServiceDependency(containerName).setService(
                    ISwitchManager.class).setCallbacks("setSwitchManager",
                    "unsetSwitchManager").setRequired(true));

            c.add(createContainerServiceDependency(containerName).setService(
                    IDataPacketService.class).setCallbacks(
                    "setDataPacketService", "unsetDataPacketService")
                    .setRequired(true));

            c.add(createContainerServiceDependency(containerName).setService(
                    IFlowProgrammerService.class).setCallbacks(
                    "setFlowProgrammerService", "unsetFlowProgrammerService")
                    .setRequired(true));
            
            c.add(createServiceDependency()
                    .setService(IController.class, "(name=Controller)")
                    .setCallbacks("setControllerService", "unsetControllerService")
                    .setRequired(true));
            /*
            c.add(createContainerServiceDependency(containerName).setService(
            		ITopologyService.class).setCallbacks(
                    "setTopologyService", "unsetTopologyService")
                    .setRequired(false));
            
            c.add(createServiceDependency().setService(IDiscoveryService
            		.class, "(name=Controller)").setCallbacks(
                    "setDiscService", "unsetDiscService")
                    .setRequired(false));
            
            c.add(createContainerServiceDependency(containerName).setService(ITopologyManager
            		.class).setCallbacks(
                    "setTopoManager", "unsetTopoManager")
                    .setRequired(false));
            */
            
    		
        	
        }
    }
    
}
