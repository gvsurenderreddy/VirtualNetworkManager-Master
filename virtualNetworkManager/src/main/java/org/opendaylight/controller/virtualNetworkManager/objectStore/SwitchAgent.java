package org.opendaylight.controller.virtualNetworkManager.objectStore;

public class SwitchAgent {
	private String agentUri = null;

	/*
	// Default Constructor <Tentative>
	public Agent(){

	}
	*/

	public SwitchAgent(String agentUri){
		this.agentUri = agentUri;
	}

	public String getAgentUri() {
		return agentUri;
	}

	public void setAgentUri(String agentUri) {
		this.agentUri = agentUri;
	}

}
