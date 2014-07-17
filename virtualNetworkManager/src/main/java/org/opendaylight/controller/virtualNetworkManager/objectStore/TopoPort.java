package org.opendaylight.controller.virtualNetworkManager.objectStore;

import org.opendaylight.controller.sal.core.NodeConnector;

public class TopoPort extends Port {

	private NodeConnector nodeConnector = null;

	public TopoPort(String MAC, NodeConnector nodeConnector) {
		super(MAC, null);
		// TODO Auto-generated constructor stub
		this.setNodeConnector(nodeConnector);
	}

	public NodeConnector getNodeConnector() {
		return nodeConnector;
	}

	public void setNodeConnector(NodeConnector nodeConnector) {
		this.nodeConnector = nodeConnector;
	}



}
