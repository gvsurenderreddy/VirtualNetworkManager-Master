package org.opendaylight.controller.virtualNetworkManager.core;

public class VNMPort {
	private String objectId = "";
    private String name = "";
    private String macAddress = "";
    private int portNumber;

    public VNMPort(){
    }

    public VNMPort(String name, String macAddr, int portNum) {
            this.setName(name);
            this.setMacAddress(macAddr);
            this.setPortNumber(portNum);
    }

    public String getObjectId() {
            return objectId;
    }

    public void setObjectId(String objectId) {
            this.objectId = objectId;
    }

    public String getName() {
            return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public String getMacAddress() {
            return macAddress;
    }

    public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
    }

    public int getPortNumber() {
            return portNumber;
    }

    public void setPortNumber(int portNumber) {
            this.portNumber = portNumber;
    }
}
