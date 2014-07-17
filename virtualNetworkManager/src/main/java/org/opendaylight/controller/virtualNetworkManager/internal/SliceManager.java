package org.opendaylight.controller.virtualNetworkManager.internal;

import org.opendaylight.controller.virtualNetworkManager.objectStore.SliceTree;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SliceManager {

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(SliceManager.class);

	/* Internal Project Globals */
	private VnmServicePojo services = null;
	private SliceTree sliceTree = null;
	private TopologyTree topoTree = null;


	public VnmServicePojo getServices() {
		return services;
	}
	public void setServices(VnmServicePojo services) {
		this.services = services;
	}
	public SliceTree getSliceTree() {
		return sliceTree;
	}
	public void setSliceTree(SliceTree sliceTree) {
		this.sliceTree = sliceTree;
	}
	public TopologyTree getTopoTree() {
		return topoTree;
	}
	public void setTopoTree(TopologyTree topoTree) {
		this.topoTree = topoTree;
	}
}
