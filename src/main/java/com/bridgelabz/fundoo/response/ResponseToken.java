package com.bridgelabz.fundoo.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseToken {
	String token;
	String responseMessage;
	int responseCode;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	@Override
	public String toString() {
		return "ResponseToken [token=" + token + ", responseMessage=" + responseMessage + ", responseCode="
				+ responseCode + "]";
	}
	

}