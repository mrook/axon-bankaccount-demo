package org.demo

import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.common.IdentifierFactory
import org.axonframework.spring.event.AxonStartedEvent
import org.demo.domain.OpenAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InitialData @Autowired constructor(private val commandGateway: CommandGateway, private val identifierFactory: IdentifierFactory) {
	private val log = KotlinLogging.logger {}

    @EventListener(AxonStartedEvent::class)
    fun initialize() {
		commandGateway.sendAndWait<OpenAccount>(OpenAccount(identifierFactory.generateIdentifier(), "mooi nummer"))
        log.info("Application started, adding initial data")
    }
}
