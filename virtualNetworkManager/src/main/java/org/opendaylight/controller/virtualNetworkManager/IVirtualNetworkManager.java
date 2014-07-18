package org.opendaylight.controller.virtualNetworkManager;



public interface IVirtualNetworkManager {

	/* Interface To Test VNM */
	public void testVnm();

	/* Interface To Create a New Slice */
	public boolean addSlice(int sliceId, String desc);

	/* Interface To Add a Switch To A Specific Slice */
	public boolean addSwitchToSlice(int sliceId, String dataPathId, String name, String desc);

	/* Interface To Add a Switch To a Specific Switch */
	public boolean addPortToSwitch(int SliceId, String dataPathId, String MAC, String desc);

	/* Interface To Register an Agent URI for a Specific Switch */
	public boolean registerAgentToSwitch(String dataPathId, String agentUri);
}
