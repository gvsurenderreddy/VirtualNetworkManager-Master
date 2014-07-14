package org.opendaylight.controller.virtualNetworkManager.core;

public interface IVirtualNetworkManager {

	public void addSwitch(VNMSwitch vswitch);
	public void deleteSWitch(VNMSwitch vnode);
	public void updateSwitch(VNMSwitch vswitch);
	public void addNode(VNMPort vnode);
	public void updateNode(VNMPort vnode);
	public void deleteNode(VNMPort vnode);
	public void addTunnel(VNMSwitch vswitch, VNMTunnel vtunnel);
	public void updateTunnel(VNMSwitch vswitch, VNMTunnel vtunnel);
	public void deleteTunnel(VNMTunnel vtunnel);
}
