package org.opendaylight.controller.virtualNetworkManager.objectStore;

import java.util.ArrayList;
import java.util.HashMap;

import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopoSwitch extends Switch {

	private Node node = null;
	private static final Logger logger = LoggerFactory
            .getLogger(TopologyTree.class);

	private HashMap<String, TopoPort> ports = null;
	private boolean state = false;

	/*
	// Default Constructor : Tentative
	public TopoSwitch(){
		super();
	}
	*/
	public TopoSwitch(String dataPathId, String name, Node node) {
		super(dataPathId, name, null);
		// TODO Auto-generated constructor stub
		this.setNode(node);
		ports = new HashMap<String, TopoPort>();
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void setPorts(HashMap<String, TopoPort> overlayPorts) {
		this.ports = overlayPorts;
	}

	/* Method to add a Port to a switch in Topology Tree */
	public TopoPort addPort(String MAC, NodeConnector nodeConnector, PortType type){

		/* check if the Port object is already in the DB */
		if(ports.containsKey(MAC)){
			logger.error("Port for MAC: " + MAC +" already added in Switch: " + this.getDataPathId());
			return null;
		}
		else {
			TopoPort port = new TopoPort(MAC, nodeConnector);
			ports.put(MAC, port);
			return port;
		}
	}

	/* Method to get a Port from a switch from Topology Tree */
	public TopoPort getPort(String MAC){

		/* check if the Port object is already in the DB */
		if(ports.containsKey(MAC)){
			return ports.get(MAC);
		}
		else {
			logger.error("No Port with MAC: " + MAC +" is added in Switch: " + getDataPathId());
			return null;
		}
	}

	public ArrayList<TopoPort> getAllPorts() {
		// TODO Auto-generated method stub
		return new ArrayList<TopoPort>(ports.values());
	}

	/* Method to delete a Port from a switch from Topology Tree */
	public boolean deletePort(String MAC){

		/* check if the Port object is already in the DB */
		if(ports.containsKey(MAC)){
			ports.remove(MAC);
			return true;
		}
		else {
			logger.error("No Port with MAC: " + MAC +" is added in Switch: " + getDataPathId());
			return false;
		}
	}
}
