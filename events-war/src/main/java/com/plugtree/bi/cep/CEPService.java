package com.plugtree.bi.cep;

import java.util.Properties;
import java.util.concurrent.Executors;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.Environment;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

public class CEPService implements Runnable {

	private static final CEPService INSTANCE = new CEPService();
	
	public static CEPService getInstance() {
		return INSTANCE;
	}

	private final StatefulKnowledgeSession ksession;
	private final Thread service;
	
	private CEPService() {
		Properties bprops = new Properties(); //TODO check what to add
		KnowledgeBuilderConfiguration bconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(bprops, getClass().getClassLoader());
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(bconf);
		kbuilder.add(new ClassPathResource("cep.drl"), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			for (KnowledgeBuilderError error : kbuilder.getErrors()) {
				System.out.println(error.toString());
			}
			throw new RuntimeException("Invalid knowledge base: " + kbuilder.getErrors());
		}
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		Properties sprops = new Properties(); //TODO check what to add
		KnowledgeSessionConfiguration sconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(sprops);
		Environment env = KnowledgeBaseFactory.newEnvironment(); //TODO check what to add 
		ksession = kbase.newStatefulKnowledgeSession(sconf, env);
		service = Executors.defaultThreadFactory().newThread(this);
		service.start();
	}
	
	@Override
	protected void finalize() throws Throwable {
		ksession.dispose();
		service.interrupt();
		super.finalize();
	}
	
	public void run() {
		ksession.fireUntilHalt();
	}

	public FactHandle insert(Object event) {
		return ksession.insert(event);
	}
}
