package org.opendaylight.controller.virtualNetworkManager.internal;

import java.util.HashMap;

public interface InternalModule {
	// Tagging interface - Mark-up interface
	// For all module inside YAON
	public boolean checkModuleDependency(HashMap<String, InternalModule> modules);
}
