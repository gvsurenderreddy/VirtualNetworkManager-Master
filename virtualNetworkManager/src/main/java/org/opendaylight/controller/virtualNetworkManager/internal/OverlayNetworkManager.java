package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.HashMap;

import org.opendaylight.controller.virtualNetworkManager.utility.TunnelAddRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlayNetworkManager implements InternalModule{

	/* Internal Globals */
	private static final Logger logger = LoggerFactory
            .getLogger(OverlayNetworkManager.class);
	private HashMap<String, InternalModule> modules = null;

	public void setModules(HashMap<String, InternalModule> modules) {
		// TODO Auto-generated method stub
		this.modules = modules;
		logger.info("Module size: " + modules.size());
	}
	/* Module specific dependency checker */
	@Override
	public boolean checkModuleDependency(HashMap<String, InternalModule> modules){
		return true;
	}
	public boolean addPortToNetwork(int sliceId, String broadcast_address, String agentUri) {
		// TODO Auto-generated method stub
		/* Create TunnelAdd request */
		TunnelAddRequest tunnelAddReq = new TunnelAddRequest(sliceId, broadcast_address);
		/* Send the TunnelAdd request to Agent */
		if(!tunnelAddReq.send(agentUri)) {
			logger.error("Tunnel add request for Slice ID: " + sliceId + " to agentUri: " + agentUri + " Failed!");
			return false;
		}
		logger.info("Tunnel add request for Slice ID: " + sliceId + " to agentUri: " + agentUri + " Successfully sent");
		return true;
	}

}
