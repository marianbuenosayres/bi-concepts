package com.plugtree.bi.api.handlers;

public class ConsumeEventException extends RuntimeException {

	private static final long serialVersionUID = 7532407997275117188L;

	public ConsumeEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsumeEventException(String message) {
		super(message);
	}

	public ConsumeEventException(Throwable cause) {
		super(cause);
	}

	
}
