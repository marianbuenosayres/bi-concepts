package com.plugtree.bi.api.defs;

public interface EventProcessorDefinition extends EventConsumerDefinition, EventProducerDefinition {

    String getProcessorType(); //stateless, stateful
}
