package io.mkeasy.webapp.exception;

public class UserSecurityException extends Exception{
	private String requestPath;
	
	public UserSecurityException(String requestPath) {
		this.requestPath = requestPath;
	}
	public String getRequestPath() {
		return requestPath;
	}
}
