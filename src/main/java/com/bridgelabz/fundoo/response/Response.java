package com.bridgelabz.fundoo.response;

import org.springframework.stereotype.Component;

@Component
public class Response {
	String responseMessage;
	int responseCode;
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
		return "Response [responseMessage=" + responseMessage + ", responseCode=" + responseCode + "]";
	}
	
}
