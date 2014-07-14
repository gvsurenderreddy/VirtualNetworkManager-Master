package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.Map;

import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.UpdateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchStateManager{

	private static final Logger logger = LoggerFactory
            .getLogger(SwitchStateManager.class);
	private VnmServicePojo services = null;




	public VnmServicePojo getServices() {
		return services;
	}

	public void setServices(VnmServicePojo services) {
		this.services = services;
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
		final String vnmSwitchId = node.getNodeIDString();
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


}
