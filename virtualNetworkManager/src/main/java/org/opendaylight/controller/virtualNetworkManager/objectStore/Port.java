package org.opendaylight.controller.virtualNetworkManager.objectStore;

public class Port {

	private String MAC = null;
	private String desc = null;

	public Port(String MAC, String desc){
		this.setMAC(MAC);
		this.setDesc(desc);
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


}
