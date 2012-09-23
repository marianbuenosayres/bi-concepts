package com.plugtree.bi.api.deploy;

public class StartException extends RuntimeException {

	private static final long serialVersionUID = -5067955740083377839L;

	public StartException(String message, Throwable cause) {
		super(message, cause);
	}

	public StartException(String message) {
		super(message);
	}

	public StartException(Throwable cause) {
		super(cause);
	}
}
