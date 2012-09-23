package com.plugtree.bi.api.defs;

import java.util.Collection;


public interface EventProducerDefinition {

	String getName();
	
	Collection<EventChannelDefinition> getTo();
}
