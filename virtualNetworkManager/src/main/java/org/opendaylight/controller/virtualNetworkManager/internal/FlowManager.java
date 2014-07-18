package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.HashMap;

import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowManager implements InternalModule{

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(FlowManager.class);
	private HashMap<String, InternalModule> modules = null;

	/* Internal Project Globals */
	private VnmServicePojo services = null;
	private TopologyTree topoTree = null;



	/* Initializing Functions */

	public VnmServicePojo getServices() {
		return services;
	}
	public void setServices(VnmServicePojo services) {
		this.services = services;
	}
	public TopologyTree getTopoTree() {
		return topoTree;
	}
	public void setTopoTree(TopologyTree topoTree) {
		this.topoTree = topoTree;
	}
	public void setModule(HashMap<String, InternalModule> modules) {
		// TODO Auto-generated method stub
		this.modules = modules;
		logger.info("Module size: " + modules.size());
	}
	/* Module specific dependency checker */
	public boolean checkModuleDependency(HashMap<String, InternalModule> modules){
		return true;
	}
}
