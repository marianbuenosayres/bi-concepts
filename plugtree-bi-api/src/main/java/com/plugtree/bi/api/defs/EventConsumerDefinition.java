package com.plugtree.bi.api.defs;

import java.util.Collection;


public interface EventConsumerDefinition {

	String getName();

	Collection<EventChannelDefinition> getFrom();
}
