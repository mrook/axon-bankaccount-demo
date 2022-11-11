package org.demo

import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.common.IdentifierFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InitialData @Autowired constructor(private val commandGateway: CommandGateway, private val identifierFactory: IdentifierFactory) {
	private val log = KotlinLogging.logger {}

    @EventListener(ApplicationPreparedEvent::class)
    fun initialize() {
        log.info("Application prepared, adding initial data")
    }
}
