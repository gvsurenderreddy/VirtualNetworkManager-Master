package org.opendaylight.controller.virtualNetworkManager.objectStore;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SliceTree {

	private static SliceTree sliceTree = null;
	private static final Logger logger = LoggerFactory
            .getLogger(SliceTree.class);

	private HashMap<String, Slice> slices = null;
	private HashMap<String, Switch> switches = null;

	public static SliceTree init(){
		if(sliceTree == null) {
			sliceTree = new SliceTree();
		}
		return sliceTree;
	}

	/* Singleton Class : private constructor */
	private SliceTree(){
		slices = new HashMap<String, Slice>();
		switches = new HashMap<String, Switch>();
	}

	/* Method to add Slice to Slice Tree */
	public Slice addSlice(int vni, String sliceDesc){

		/* check if the slice object is already in the DB */
		if(slices.containsKey(new Integer(vni).toString())){
			logger.error("SLice already added in SliceTree");
			//return slices.get(new Integer(vni).toString());
			return null;
		}
		else {
			Slice slice = new Slice(vni, sliceDesc);
			slices.put(new Integer(slice.getVNI()).toString(), slice);
			return slice;
		}
	}

	/* Method to get Slice from Slice Tree */
	public Slice getSlice(int vni){
		/* check if the slice object is already in the DB */
		if(slices.containsKey(new Integer(vni).toString())){
			return slices.get(new Integer(vni).toString());
		}
		else {
			logger.error("No Slice is found in SliceTree!");
			return null;
		}
	}

	/* Method to get all Slices from the Tree */
	public ArrayList<Slice> getAllSlice(){
		return new ArrayList<Slice>(slices.values());
	}

	/* Method to delete Slice from Slice Tree */
	public boolean deleteSlice(int vni){
		/* check if the slice object is alredy in the DB */
		if(slices.containsKey(new Integer(vni).toString())){
			slices.remove(new Integer(vni).toString());
			return true;
		}
		else {
			logger.error("No Slice is found!");
			return false;
		}
	}

	/* Add switch by DataPath ID */
	public Switch addSwitch(String datapathId, String name, String desc) {
		if(switches.containsKey(datapathId)) {
			logger.error("Switches of datapath ID: " + datapathId + " is already in the SliceTree");
			return null;
		}
		else {
			Switch swth = new Switch(datapathId, name, desc);
			switches.put(datapathId, swth);
			return swth;
		}
	}

	/* Get Switch by dataPath ID */
	public Switch getSwitch(String datapathId) {
		if(switches.containsKey(datapathId)) {

			return switches.get(datapathId);
		}
		else {
			logger.error("Switches of datapath ID: " + datapathId + " not found in SliceTree");
			return null;
		}
	}

	/* Get all switches */
	public ArrayList<Switch> getAllSwitch() {
		return new ArrayList<Switch>(switches.values());
	}

	/* Delete Switch by dataPath ID */
	public boolean deleteSwitch(String dataPathId){

		if(switches.containsKey(dataPathId)){
			switches.remove(dataPathId);
			return true;
		}
		return false;
	}
}
