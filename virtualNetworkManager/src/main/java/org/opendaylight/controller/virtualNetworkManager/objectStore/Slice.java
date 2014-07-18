package org.opendaylight.controller.virtualNetworkManager.objectStore;

import java.util.HashMap;

import org.opendaylight.controller.virtualNetworkManager.core.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slice {

	private int VNI;
	private String desc = null;
	private HashMap<String, SliceSwitch> switches = null;
	private Operation switchAddOperation = null;

	private static final Logger logger = LoggerFactory
            .getLogger(SliceTree.class);

	/*
	// Default Cons <Tentative>
	public Slice(){

	}
	 */
	public Slice(int VNI, String desc){
		this.VNI = VNI;
		this.desc = desc;
		this.switches = new HashMap<String, SliceSwitch>();
	}


	public int getVNI() {
		return VNI;
	}

	public void setVNI(int VNI) {
		this.VNI = VNI;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* Method to add a Switch to a specific Slice in Slice Tree */
	public SliceSwitch addSwitch(Switch rawSwitch){

		/* check if the slice object is already in the DB */
		if(switches.containsKey(rawSwitch.getDataPathId())){
			logger.error("Switch already added in SliceTree for Slice: " + VNI);
			//return switches.get(datapathId);
			return null;
		}
		else {
			SliceSwitch swth = new SliceSwitch(rawSwitch);
			switches.put(rawSwitch.getDataPathId(), swth);
			return swth;
		}
	}

	/* Method to get a Switch to a specific Slice in Slice Tree */
	public SliceSwitch getSwitch(String datapathId){

		/* check if the switch object is already in the DB */
		if(switches.containsKey(datapathId)){
			return switches.get(datapathId);
		}
		else {
			logger.error("No Switch is found in SliceTree for Slice: " + VNI);
			return null;
		}
	}

	/* Method to Delete a Switch from a Slice in Slice Tree */
	public boolean deleteSwitch(String datapathId){

		/* check if the switch object is already in the DB */
		if(switches.containsKey(datapathId)){
			switches.remove(datapathId);
			return true;
		}
		else {
			logger.error("No Switch is found in SliceTree for Slice: " + VNI);
			return false;
		}
	}

	public Operation getSwitchAddOperation() {
		return switchAddOperation;
	}


	public void setSwitchAddOperation(Operation switchAddOperation) {
		this.switchAddOperation = switchAddOperation;
	}
}
