package org.opendaylight.controller.virtualNetworkManager;



public interface IVirtualNetworkManager {

	/* Interface To Test VNM */
	public void testVnm();

	/* Interface To Create a New Slice */
	public void addSlice(int sliceId, String desc);

	/* Interface To Add a Switch To A Specific Slice */
	public void addSwitchToSlice(int sliceId, String dataPathId, String name, String port, String desc);

	/* Interface To Add a Switch To a Specific Switch */
	public void addPortToSwitch(String dataPathId, String MAC, String desc);

	/* Interface To Register an Agent URI for a Specific Switch */
	public void registerAgentToSwitch(String dataPathId, String agentUri);
}
