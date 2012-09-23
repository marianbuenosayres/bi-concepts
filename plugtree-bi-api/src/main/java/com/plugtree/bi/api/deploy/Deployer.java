package com.plugtree.bi.api.deploy;

public interface Deployer {

	void deploy(Deployable component) throws DeployException;
}
