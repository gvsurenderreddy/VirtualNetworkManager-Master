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

	public static SliceTree init(){
		if(sliceTree == null) {
			sliceTree = new SliceTree();
		}
		return sliceTree;
	}

	/* Singleton Class : private constructor */
	private SliceTree(){
		slices = new HashMap<String, Slice>();
	}

	/* Method to add Slice to Slice Tree */
	public Slice addSlice(int vni, String sliceDesc){

		/* check if the slice object is already in the DB */
		if(slices.containsKey(new Integer(vni).toString())){
			logger.error("SLice already added in SliceTree");
			return slices.get(new Integer(vni).toString());
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

}
