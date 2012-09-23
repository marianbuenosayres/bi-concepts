package com.plugtree.bi.api.deploy;

public class DeployException extends RuntimeException {

	private static final long serialVersionUID = 72327614507410306L;

	public DeployException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeployException(String message) {
		super(message);
	}

	public DeployException(Throwable cause) {
		super(cause);
	}
}
