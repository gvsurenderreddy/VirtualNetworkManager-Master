package org.opendaylight.controller.virtualNetworkManager.internal;

import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowManager {

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(FlowManager.class);

	/* Internal Project Globals */
	private VnmServicePojo services = null;
	private TopologyTree topoTree = null;


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
}
