package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.Map;

import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.UpdateType;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchEventManager{

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(SwitchEventManager.class);

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

	public void switchAdded(Node node) {
		// TODO Auto-generated method stub
		logger.info("New OpenFlow Switch Added Info has reached to VNM!");
	}

	public void switchChanged(Node node){
		logger.info("New OpenFlow Switch Modified Info has reached to VNM!");
	}

	public void switchDeleted(Node node) {
		// TODO Auto-generated method stub
		logger.info("New OpenFlow Switch Deleted Info has reached to VNM!");
	}

	public void switchChanged(Node node, UpdateType type, Map<String, Property> propMap) {
		// TODO Auto-generated method stub
		//final String vnmSwitchId = node.getNodeIDString();
		//VNMSwitch opfSwitch = getOPFSwitchById(opfSwitchId);
		switch (type) {
        	case ADDED:
        		this.switchAdded(node);
        		break;
        	case CHANGED:
	           // if (opfSwitch == null) {
	                // Create a new OPF Switch Entry
	         //       this.tappingApp.createOPFSwitch(node);
	          //  	this.switchAdded(node);
	          //  }
	           // else{
	            this.switchChanged(node);
	          //  }
	            break;

        	case REMOVED:
	        /*    if (opfSwitch != null) {
	        //        this.tappingApp.removeOPFSwitch(node);
	            }
	            */
	        	this.switchDeleted(node);
	        	break;
        	default:
        		logger.warn("Unknown Type OpenFlow Switch Change Info has reached to VNM!");
        }
	}

	public void portChanged(NodeConnector nodeConnector, UpdateType type){
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



}
