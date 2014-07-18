package org.opendaylight.controller.virtualNetworkManager.utility;

import net.sf.json.JSONObject;

import org.apache.http.client.ResponseHandler;

public abstract class JsonRequest {

	protected JSONObject jsonObj;
	protected String jsonString;
	protected ResponseHandler<?> responseHandler;
	//protected UserData<?> usrData; /* tentative */

	//public void setUsrData(UserData<?> usrData) {
	//	this.usrData = usrData;
	//}

	public JsonRequest(){
		jsonObj = new JSONObject();
		jsonString  = null;
		responseHandler = null;
	//	usrData = null;
	}

	/* send the JSON request in the specified agent URI */
	public abstract boolean send(String agentUri);

}
