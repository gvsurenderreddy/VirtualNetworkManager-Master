package org.opendaylight.controller.virtualNetworkManager.internal;

public enum Module {
	    OverlayNetworkManager("OverlayNetworkManager"),
	    FlowManager("FlowManager"),
	    SliceManager("SliceManager"),
	    SwitchEventManager("SwitchEventManager");

	    private final String name;

	    private Module(String s) {
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
