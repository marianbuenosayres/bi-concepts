package com.plugtree.bi.cep;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
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

	private CEPEventListener listener;
	private final StatefulKnowledgeSession ksession;
	private final Thread service;
	
	private CEPService() {
		Properties kbuilderProps = new Properties(); //TODO check what to add
		Properties kbaseProps = new Properties();
		Properties ksessionProps = new Properties();
		Environment env = KnowledgeBaseFactory.newEnvironment(); //TODO check what to add 
		KnowledgeBuilderConfiguration bconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(kbuilderProps, getClass().getClassLoader());
		KnowledgeBaseConfiguration kbconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(kbaseProps, getClass().getClassLoader());
		KnowledgeSessionConfiguration sconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(ksessionProps);
		kbconf.setOption(EventProcessingOption.STREAM);
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(bconf);
		kbuilder.add(new ClassPathResource("cep.drl"), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			for (KnowledgeBuilderError error : kbuilder.getErrors()) {
				System.out.println(error.toString());
			}
			throw new RuntimeException("Invalid knowledge base: " + kbuilder.getErrors());
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbconf);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		ksession = kbase.newStatefulKnowledgeSession(sconf, env);
		applyListeners(ksession);
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
		System.out.println("NEW EVENT: " + event);
		return ksession.insert(event);
	}
	
	protected void applyListeners(StatefulKnowledgeSession ksession) {
		this.listener = new CEPEventListener(ksession);
	}
	
	protected Map<String, Long> getFiredRules() {
		return this.listener.getFiredRules();
	}
}
