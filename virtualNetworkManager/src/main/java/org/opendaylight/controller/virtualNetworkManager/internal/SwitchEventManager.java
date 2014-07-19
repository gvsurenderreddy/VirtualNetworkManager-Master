package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.HashMap;
import java.util.Map;

import org.opendaylight.controller.sal.core.MacAddress;
import org.opendaylight.controller.sal.core.Name;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.Property;
import org.opendaylight.controller.sal.core.UpdateType;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopoPort;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopoSwitch;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchEventManager implements InternalModule{

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(SwitchEventManager.class);
	private HashMap<String, InternalModule> modules = null;

	/* Internal Project Globals */
	private ServicePojo services = null;
	private TopologyTree topoTree = null;



	/* Initializing Function */

	public ServicePojo getServices() {
		return services;
	}

	public void setServices(ServicePojo services) {
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
	@Override
	public boolean checkModuleDependency(HashMap<String, InternalModule> modules){
		return true;
	}



	/* Feature Functions */

	public boolean switchChanged(Node node, UpdateType type, Map<String, Property> propMap) {

		/* Check type of switch change notification */
		switch (type) {

        	case ADDED:
        		if(!this.switchAdded(node)){
        			logger.error("Switch addition failed !");
        			return false;
        		}
        		break;

        	case CHANGED:
	            if(!this.switchModified(node)){
	            	logger.error("Switch modification failed !");
        			return false;
	            }
	            break;

        	case REMOVED:
	        	if(!this.switchDeleted(node)){
	        		logger.error("Switch modification failed !");
        			return false;
	        	}
	        	break;

        	default:
        		logger.error("Unknown Type OpenFlow Switch Notification has reached to VNM!");
        		return false;
        }
		return true;
	}

	public boolean portChanged(NodeConnector nodeConnector, UpdateType type){

        switch (type) {
	        case ADDED:
	        	if(!this.portAdded(nodeConnector)){
	        		logger.error("NodeConnector addition failed !");
	        		return false;
	        	}
	        	break;

	        case CHANGED:
	        	if(!this.portModified(nodeConnector)){
	        		logger.error("NodeConnector modification failed !");
	        		return false;
	        	}
	        	break;

	        case REMOVED:
	        	if(!this.portDeleted(nodeConnector)){
	        		logger.error("NodeConnector removeal failed");
	        		return false;
	        	}
	        	break;

	        default:
	            logger.error("Unknown NodeConnector notification received");
	            return false;
        }
        return true;
	}



	/* Internal Function */

	private boolean switchAdded(Node node) {

		logger.info("New OpenFlow Switch Added Info has reached to VNM!");

		/* Parse the Node info to get Node Property/Info */

		/* Get Name */
		Property nameProp = services.getSwitchManager().getNodeProp(node, "name"); /*XXX*/
		String name = nameProp.getName();
		if(name == null) {
			logger.warn("Switch Name could not be resolved !");
		}

		/* Get Data path ID */
		String dpId = node.getNodeIDString();
		if(dpId == null){
			logger.error("Switch DpId could not be resolved !");
			return false;
		}


		/* Add Switch to TopoLogy Tree */

		/* check if switch is already added to topology Tree */
		TopoSwitch swth = topoTree.getSwitch(dpId);
		if(swth == null){
			topoTree.addSwitch(dpId, name, node);
		}
		else {
			/* Check if duplicate switch add request for active switch*/
			if(swth.isState() == true) {
				logger.warn("Duplicate switch addition info found for same datapath. Replacing old information !");
				swth.setName(name);
				swth.setNode(node);
				swth.setPorts(null);
			}
			else {
				logger.info("Switch is being active using switch specific information !");
				swth.setName(name);
				swth.setNode(node);
				swth.setState(true);
			}
		}
		return true;
	}

	private boolean switchModified(Node node){

		logger.info("New OpenFlow Switch Modified Info has reached to VNM!");

		/* Get Name */
		Property nameProp = services.getSwitchManager().getNodeProp(node, Name.NamePropName); /*XXX*/
		String name = nameProp.getStringValue();
		if(name == null) {
			logger.warn("Switch Name could not be resolved !");
		}

		/* Get Data path ID */
		String dpId = node.getNodeIDString();
		if(dpId == null){
			logger.error("Switch DpId could not be resolved !");
			return false;
		}

		/* Add Switch to TopoLogy Tree */

		/* check if switch is added to topology Tree */
		TopoSwitch swth = topoTree.getSwitch(dpId);
		if(swth == null){
			topoTree.addSwitch(dpId, name, node);
		}
		else {
			/* Check if modification info for inactive switch*/
			/*
			if(swth.isState() == true) {
				swth.setName(name);
				swth.setNode(node);
				swth.setHostPorts(null);
				swth.setOverlayPorts(null);
				swth.setSpecialPorts(null);
			}
			else {
				logger.info("Switch is being active using switch modification information !");
				swth.setName(name);
				swth.setNode(node);
				swth.setState(true);
			}
			*/
		}


		return true;
	}

	private boolean switchDeleted(Node node) {

		logger.info("New OpenFlow Switch Deleted Info has reached to VNM!");
		return true;
	}


	private boolean portAdded(NodeConnector nodeConnector) {

		logger.info("New Port Added Info has reached to VNM!");
		TopoPort port = null;

		/* Get Node of the port */
		Node node = nodeConnector.getNode();
		if(node == null){
			logger.error("Node information could not found for NodeConnector: {}", nodeConnector);
			return false;
		}

		/* Get Data-path ID from Node to identify the switch */
		String dataPathId = node.getNodeIDString();
		if(dataPathId == null) {
			logger.error("Datapath id could not be resolved of the switch for NodeConnector: {}", nodeConnector);
			return false;
		}

		/* Parse NodeConnector info to get Port info */
		Property macAddrress = services.getSwitchManager().getNodeConnectorProp(nodeConnector, MacAddress.name);
		String portMac = macAddrress.getStringValue();
		if(portMac == null) {
			logger.error("Port mac could not be resolved for port: {}", nodeConnector);
			return false;
		}
		Property name = services.getSwitchManager().getNodeConnectorProp(nodeConnector, Name.NamePropName);
		String portName = name.getStringValue();

		/* Check if switch exist in the Topo-Tree with the datapath ID */
		TopoSwitch swth = topoTree.getSwitch(dataPathId);
		if(swth == null){
			logger.info("Switch not yet added to the topo Tree: creating Inactive Topo Switch !");
			swth = new TopoSwitch(dataPathId, null, node);
			swth.setState(false);
		}
		else {
			/* Check if port is already added into the switch */
			port = swth.getPort(portMac);
			if(port != null){
				logger.warn("Port is already attached to switch, resetting details!"); /*XXX */
				port.setNodeConnector(nodeConnector);
				port.setName(portName);
				port.setType(null);
				return false;
			}
		}
		port = swth.addPort(portMac, nodeConnector, null);
		port.setName(portName);
		return true;
	}

	private boolean portDeleted(NodeConnector nodeConnector) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean portModified(NodeConnector nodeConnector) {
		// TODO Auto-generated method stub
		return false;
	}

}
