package com.plugtree.bi.api.defs;

import java.util.Collection;

public interface EventChannelDefinition {

	String getName();

	Collection<EventConsumerDefinition> getOrigin();

	Collection<EventProducerDefinition> getDestination();
}
