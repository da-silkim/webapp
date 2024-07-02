package com.bluenetworks.webapp.common;

public class ErrorMessage extends RuntimeException {
	public ErrorMessage(){
		super();
	}
	public ErrorMessage(String message){
		super(message);
	}
}