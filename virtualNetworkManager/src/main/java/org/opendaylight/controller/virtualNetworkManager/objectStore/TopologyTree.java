package org.opendaylight.controller.virtualNetworkManager.objectStore;

import java.util.HashMap;

import org.opendaylight.controller.sal.core.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopologyTree {

	private static TopologyTree topoTree = null;
	private static final Logger logger = LoggerFactory
            .getLogger(TopologyTree.class);

	private HashMap<String, TopoSwitch> switches = null;

	public static TopologyTree init(){
		if(topoTree == null) {
			topoTree = new TopologyTree();
		}
		return topoTree;
	}

	/* Singleton Class : private constructor */
	private TopologyTree(){
		switches = new HashMap<String, TopoSwitch>();
	}

	/* Method to add Switch to Topology Tree */
	public TopoSwitch addSwitch(String dataPathId, String name, Node node){

		/* check if the switch object is already in the DB */
		if(switches.containsKey(dataPathId)){
			logger.error("Switch for datapath: " + dataPathId +" already added in TopologyTree");
			return switches.get(dataPathId);
		}
		else {
			TopoSwitch swth = new TopoSwitch(dataPathId, name, node);
			switches.put(dataPathId, swth);
			return swth;
		}
	}

	/* Method to get a switch from Topology Tree */
	public TopoSwitch getSwitch(String dataPathId){

		/* check if the switch object is already in the DB */
		if(switches.containsKey(dataPathId)){
			return switches.get(dataPathId);
		}
		else {
			logger.error("No Switch with datapath: " + dataPathId +" is added in TopologyTree");
			return null;
		}
	}

	/* Method to delete a switch from Topology Tree */
	public boolean deleteSwitch(String dataPathId){

		/* check if the switch object is already in the DB */
		if(switches.containsKey(dataPathId)){
			switches.remove(dataPathId);
			return true;
		}
		else {
			logger.error("No Switch with datapath: " + dataPathId +" is added in TopologyTree");
			return false;
		}
	}
}
