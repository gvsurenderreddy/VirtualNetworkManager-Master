package org.opendaylight.controller.virtualNetworkManager.objectStore;

public enum PortType {
	Special("Special"),
    Overlay("Overlay"),
    Host("Host");

    private final String name;

    private PortType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    @Override
	public String toString(){
       return name;
    }
}
