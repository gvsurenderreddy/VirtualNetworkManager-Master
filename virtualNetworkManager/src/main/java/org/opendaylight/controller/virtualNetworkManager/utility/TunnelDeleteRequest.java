package org.opendaylight.controller.virtualNetworkManager.utility;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


public class TunnelDeleteRequest extends JsonRequest {

	@Override
	public boolean send(String agentUri) {
		// TODO Auto-generated method stub
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpTransactionComplete responseHandler = new HttpTransactionComplete();
		//responseHandler.setUsrData(usrData);
		//responseHandler.setUsrData();
		this.responseHandler = responseHandler;
		try {
			HttpDelete request = new HttpDelete(agentUri);
			if(this.responseHandler != null) {
		    	httpClient.execute(request, this.responseHandler);
		    }
		    else {
		    	httpClient.execute(request);
		    	throw new Exception("Response handler is not set for HTTP request");
		    }
		}
		catch(Exception ex) {

			return false;
		}
		return true;
	}

}
