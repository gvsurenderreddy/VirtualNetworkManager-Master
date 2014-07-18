package org.opendaylight.controller.virtualNetworkManager.objectStore;

import java.util.HashMap;

import org.opendaylight.controller.virtualNetworkManager.core.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SliceSwitch {

    private HashMap<String, SlicePort> ports = null;
    private Operation portAddOperation = null;
    private Switch swth = null;

	private static final Logger logger = LoggerFactory
    .getLogger(SliceTree.class);


	/*
	// Default Constructor <Tenative>
	public Switch(){

	}
	*/

	public SliceSwitch(Switch swth) {
		this.swth = swth;
		this.ports = new HashMap<String, SlicePort>();
	}


	/* Interface to add port to a switch for a specific slice in Slice Tree */
	public SlicePort addPort(String MAC, String desc){

		/* Check if Port is already attached in switch */
		if(ports.containsKey(MAC)){
			logger.error("Port of MAC: (" + MAC + ") is already added in SliceTree for Switch: " + swth.getDataPathId());
			return ports.get(MAC);
		}
		else {
			SlicePort port = new SlicePort(MAC, desc);
			ports.put(MAC, port);
			return port;
		}
	}

	/* Interface to get port from a switch for a specific slice in Slice Tree */
	public SlicePort getPort(String MAC){

		/* Check if Port is already attached in switch */
		if(ports.containsKey(MAC)){
			return ports.get(MAC);
		}
		else {
			logger.error("No port found for MAC: (" + MAC + ") in switch: " + swth.getDataPathId());
			return null;
		}
	}


	/* Interface to delete port from a switch for a specific slice in Slice Tree */
	public boolean deletePort(String MAC){

		/* Check if Port is already attached in switch */
		if(ports.containsKey(MAC)){
			ports.remove(MAC);
			return true;
		}
		else {
			logger.error("No port found for MAC: (" + MAC + ") in switch: " + swth.getDataPathId());
			return false;
		}
	}

	public Operation getPortAddOperation() {
		return portAddOperation;
	}

	public void setPortAddOperation(Operation portAddOperation) {
		this.portAddOperation = portAddOperation;
	}

    public Switch getSwth() {
		return swth;
	}

}
