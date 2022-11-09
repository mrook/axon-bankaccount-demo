package org.demo;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitialData {
	private final CommandGateway commandGateway;
	private final IdentifierFactory identifierFactory;

	@Autowired
	public InitialData(CommandGateway commandGateway, IdentifierFactory identifierFactory) {
		this.commandGateway = commandGateway;
		this.identifierFactory = identifierFactory;
	}

	@EventListener(ApplicationPreparedEvent.class)
	public void initialize() {
		log.info("Application prepared, adding initial data");
	}
}
