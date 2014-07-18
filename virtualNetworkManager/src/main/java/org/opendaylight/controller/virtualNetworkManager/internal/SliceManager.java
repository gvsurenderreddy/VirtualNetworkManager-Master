package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.ArrayList;
import java.util.HashMap;

import org.opendaylight.controller.virtualNetworkManager.objectStore.Slice;
import org.opendaylight.controller.virtualNetworkManager.objectStore.SlicePort;
import org.opendaylight.controller.virtualNetworkManager.objectStore.SliceSwitch;
import org.opendaylight.controller.virtualNetworkManager.objectStore.SliceTree;
import org.opendaylight.controller.virtualNetworkManager.objectStore.Switch;
import org.opendaylight.controller.virtualNetworkManager.objectStore.SwitchAgent;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopoPort;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopoSwitch;
import org.opendaylight.controller.virtualNetworkManager.objectStore.TopologyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SliceManager implements InternalModule{

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(SliceManager.class);
	private HashMap<String, InternalModule> modules = null;
	private final String broadcast_address = "100.10.0.1";
	/* Statically defined wait time should be changed */
	private final long timeOut = 1000;

	/* Internal Project Globals */
	private VnmServicePojo services = null;
	private SliceTree sliceTree = null;
	private TopologyTree topoTree = null;



	/* Initializing function */

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
	public void setModule(HashMap<String, InternalModule> modules) {
		// TODO Auto-generated method stub
		this.modules = modules;
		logger.info("Module size: " + modules.size());
	}
	/* Module specific dependency checker */
	@Override
	public boolean checkModuleDependency(HashMap<String, InternalModule> modules){
		/* check if Overlay network manager is available */
		if(modules.containsKey("OverlayNetworkManager") == false){
			return false;
		}
		/* Check if Flow manager is Available */
		if(modules.containsKey("FlowManager") == false){
			return false;
		}
		return true;
	}



	/* Slice Operations */

	/* Add Slice Operation */
	public boolean addSlice(int sliceId, String desc){

		/* Add slice to Slice Tree */
		Slice slice = sliceTree.addSlice(sliceId, desc);
		if (slice == null){
			logger.error("The Slice with SliceId: " + sliceId + " already Exist in SliceTree!");
			return false;
		}
		return true;
	}

	/* Add Switch operation to a Slice */
	public boolean addSwitch(int sliceId, String dataPathId, String name, String desc){

		/* Get slice to Slice Tree */
		Slice slice = sliceTree.getSlice(sliceId);
		if(slice == null){
			logger.error("The Slice with SliceId: " + sliceId + " don't Exist in SliceTree!");
			return false;
		}

		/* Get any pre-configured switch in SliceTree (Could be created when Agent gets added)*/
		Switch rawSwth = sliceTree.getSwitch(dataPathId);
		if(rawSwth != null){
			rawSwth.setName(name);
			rawSwth.setDesc(desc);
		}
		else {
			/* No pre configured switch */
			/* Add new switch to the Slice Tree */
			rawSwth = sliceTree.addSwitch(dataPathId, name, desc);
			if(rawSwth == null){
				logger.error("The Switch with Datapath Id: " + dataPathId + " Already Exist in SliceTree!");
				return false;
			}
		}

		/* Add slice switch to the Specific Slice in Slice Tree */
		SliceSwitch swth = slice.addSwitch(rawSwth);
		if(swth == null){
			logger.error("The Switch with Datapath Id: " + dataPathId + " Already Exist in Slice!");
			return false;
		}
		return true;
	}

	/* Add port operation to a switch */
	public boolean addPort(int sliceId, String dataPathId, String MAC, String desc){

		OverlayNetworkManager overlayNetworkManager = (OverlayNetworkManager)modules.get(Module.OverlayNetworkManager.toString());
		FlowManager flowManager = (FlowManager)modules.get(Module.FlowManager.toString());
		TopoPort tPort = null;

		/* Get slice to Slice Tree */
		Slice slice = sliceTree.getSlice(sliceId);
		if(slice == null){
			logger.error("The Slice with SliceId: " + sliceId + " don't Exist in SliceTree!");
			return false;
		}

		/* get switch for the Specific Switch for slice in Slice Tree */
		SliceSwitch swth = slice.getSwitch(dataPathId);
		if(swth == null){
			logger.error("The Switch with Datapath Id: " + dataPathId + " does not Exist in Slice!");
			return false;
		}

		/* Add port to the specific switch in Slice Tree */
		SlicePort port = swth.addPort(MAC, desc);
		if(port == null){
			logger.error("The Port with MAC: " + MAC + " Already Exist in Switch!");
			return false;
		}

		/* Get agent Uri */
		Switch rawSwth = sliceTree.getSwitch(dataPathId);
		if(rawSwth == null){
			logger.error("Raw switch not found for Switch with Datapath ID: " + dataPathId);
			return false;
		}
		SwitchAgent agent = rawSwth.getAgent();
		if(agent == null){
			logger.error("No agent is configured for Switch with Datapath ID: " + dataPathId);
			return false;
		}
		if(agent.getAgentUri() == null) {
			logger.error("Agent Uri for switch with Datapath Id: " + dataPathId + " is not set");
			return false;
		}

		/* Add port to overlay network */
		if(!overlayNetworkManager.addPortToNetwork(sliceId, broadcast_address, agent.getAgentUri())){
			logger.error("Port add to overlay network failed");
			return false;
		}

		/* wait for Port to be appeared */
		tPort = waitForPort(sliceId, dataPathId);
		return true;
	}

	/* add Agent to a specific switch */
	public boolean addAgent(String dataPathId, String agentUri){

		Switch swth = null;
		SwitchAgent agent = new SwitchAgent(agentUri);
		/* Check if Switch with dataPath already exist */
		swth = sliceTree.getSwitch(dataPathId);
		if(swth != null) {
			swth.setAgent(agent);
			return true;
		}
		else {
			swth = new Switch(dataPathId, null, null);
			swth.setAgent(agent);
			return true;
		}
	}



	/* Internal function for slice manager */

	/* Wait for to be added in Topology DB */
	private TopoPort waitForPort(int sliceId, String dataPathId) {

		TopoSwitch swth = topoTree.getSwitch(dataPathId);
		if(swth == null){
			logger.error("Switch of Datapath ID (" + dataPathId + ") information not found in Topology Tree <switch might not be connected with ODL> ");
			return null;
		}
		else {
			while(true){
				ArrayList<TopoPort> ports = swth.getAllPorts();
				for(TopoPort port : ports){
					String portname = generatePortName(sliceId);
					if(port.getName().equals(portname)){
						return port;
					}
				}
				try {
					Thread.sleep(timeOut);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/* Generate port name which should be added by agent */
	private String generatePortName(int sliceId){
		return new String("vxlan" + sliceId);
	}
}

