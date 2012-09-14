package com.plugtree.bi.cep;

import java.util.HashMap; 
import java.util.Map;

import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import org.drools.event.rule.RuleFlowGroupActivatedEvent;
import org.drools.event.rule.RuleFlowGroupDeactivatedEvent;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.runtime.StatefulKnowledgeSession;

public class CEPEventListener implements WorkingMemoryEventListener, AgendaEventListener {
	
	private final Map<String, Long> firedRules = new HashMap<String, Long>();
	
	private final StatefulKnowledgeSession ksession;

	public CEPEventListener(StatefulKnowledgeSession ksession) {
		super();
		this.ksession = ksession;
		this.ksession.addEventListener((AgendaEventListener) this);
		this.ksession.addEventListener((WorkingMemoryEventListener) this);
	}

	public void activationCancelled(ActivationCancelledEvent event) { }
	public void activationCreated(ActivationCreatedEvent event) { }
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) { }
	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) { }
	public void agendaGroupPopped(AgendaGroupPoppedEvent event) { }
	public void agendaGroupPushed(AgendaGroupPushedEvent event) { }
	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) { }
	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) { }
	public void objectInserted(ObjectInsertedEvent event) { }
	public void objectRetracted(ObjectRetractedEvent event) { }
	public void objectUpdated(ObjectUpdatedEvent event) { }
	public void beforeActivationFired(BeforeActivationFiredEvent event) { }
	public void afterActivationFired(AfterActivationFiredEvent event) { 
		String key = event.getActivation().getRule().getName();
		synchronized (this) {
			Long value = firedRules.get(key);
			if (value == null) {
				value = Long.valueOf(0);
			}
			firedRules.put(key, value + 1);
		}
	}

	public Map<String, Long> getFiredRules() {
		return new HashMap<String, Long>(this.firedRules);
	}
}
