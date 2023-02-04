package org.demo.configuration

import org.axonframework.common.IdentifierFactory
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventSourcingConfiguration {
    @Bean
	fun identifierFactory(): IdentifierFactory {
        return IdentifierFactory.getInstance()
    }

	@Bean
	fun inMemoryEventStorageEngine(): InMemoryEventStorageEngine = InMemoryEventStorageEngine()
}
