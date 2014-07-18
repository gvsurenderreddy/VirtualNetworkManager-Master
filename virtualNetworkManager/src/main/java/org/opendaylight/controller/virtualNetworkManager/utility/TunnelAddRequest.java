package org.opendaylight.controller.virtualNetworkManager.utility;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jettison.json.JSONException;

public class TunnelAddRequest extends JsonRequest {

	public TunnelAddRequest(int vnetId, String address){
	    try {
	    	/* Virtual network identifier */
			super.jsonObj.accumulate("vni", vnetId);
			/* Add broadcast address */
			super.jsonObj.accumulate("broadcast", address);
			super.jsonString = super.jsonObj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean send(String agentUri) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpTransactionComplete responseHandler = new HttpTransactionComplete();
		//responseHandler.setUsrData(null);
		this.responseHandler = responseHandler;
		try {
		    HttpPost request = new HttpPost(agentUri);
		    StringEntity params = new StringEntity(jsonString);
		    request.addHeader("content-type", "application/x-www-form-urlencoded");
		    request.setEntity(params);
		    if(this.responseHandler != null) {
		    	httpClient.execute(request, this.responseHandler);
		    }
		    else {
		    	httpClient.execute(request);
		    	throw new Exception("Response handler is not set for HTTP request");
		    }
		} catch (Exception ex) {
		    // handle exception here
			return false;
		} finally {
		    try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}
