package com.bridgelabz.fundoo.utility;

import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;

public class ResponseHelper {
	public static Response statusResponse(int code, String message) {
		Response statusResponse = new Response();
		statusResponse.setResponseMessage(message);
		statusResponse.setResponseCode(code);
		return statusResponse;
	}

public static ResponseToken statusInfo(String statusMessage, int statusCode){
	ResponseToken response = new ResponseToken();
	
	response.setResponseCode(statusCode);
	
	response.setResponseMessage(statusMessage);

	return response;
}

public static ResponseToken tokenStatusInfo(String statusMessage,int statusCode,String token){
	ResponseToken tokenResponse = new ResponseToken();
	
	tokenResponse.setResponseMessage(statusMessage);
	tokenResponse.setResponseCode(statusCode);
	tokenResponse.setToken(token);

	return tokenResponse;
}
public static Response statusResponseInfo(String statusMessage, int statusCode) {
	Response response = new Response();

	response.setResponseCode(statusCode);
	response.setResponseMessage(statusMessage);
	return response;
}
}