package org.opendaylight.controller.virtualNetworkManager.objectStore;

public class Switch {

	private String dataPathId = null;
	private String name = null;
	private String desc = null;

	public Switch(String dataPathId, String name, String desc){
		this.setDataPathId(dataPathId);
		this.setDesc(desc);
		this.setName(name);
	}

	public String getDataPathId() {
		return dataPathId;
	}

	public void setDataPathId(String dataPathId) {
		this.dataPathId = dataPathId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
