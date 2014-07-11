package org.opendaylight.controller.virtualNetworkManager.core;

public interface IVirtualNetworkManager {

	public void addSwitch(VNMSwitch vswitch);
	public void deleteSWitch(VNMSwitch vnode);
	public void updateSwitch(VNMSwitch vswitch);
	public void addNode(VNMNode vnode);
	public void updateNode(VNMNode vnode);
	public void deleteNode(VNMNode vnode);
	public void addTunnel(VNMSwitch vswitch, VNMTunnel vtunnel);
	public void updateTunnel(VNMSwitch vswitch, VNMTunnel vtunnel);
	public void deleteTunnel(VNMTunnel vtunnel);
}
