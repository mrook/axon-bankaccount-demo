package org.demo.configuration

import org.axonframework.common.IdentifierFactory
import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.common.jdbc.DataSourceConnectionProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.TokenSchema
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.upcasting.event.EventUpcaster
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.demo.upcasters.AccountOpenedUpcaster
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.lang.management.ManagementFactory
import java.sql.SQLException
import java.time.Duration
import javax.sql.DataSource

@Configuration
class EventSourcingConfiguration {
    @Bean
	fun identifierFactory(): IdentifierFactory {
        return IdentifierFactory.getInstance()
    }

    private fun eventSchema(version: Int): EventSchema {
        return EventSchema.builder().eventTable(String.format("events_%d", version)).snapshotTable("snapshot_events")
                .globalIndexColumn("id").aggregateIdentifierColumn("aggregate_id").sequenceNumberColumn("sequence_number").typeColumn("type")
                .eventIdentifierColumn("event_id").metaDataColumn("metadata").payloadColumn("payload").payloadRevisionColumn("payload_revision")
                .payloadTypeColumn("payload_type").timestampColumn("timestamp").build()
    }

    private fun tokenSchema(): TokenSchema {
        return TokenSchema.builder().setTokenTable("tokens").setProcessorNameColumn("processor_name").setSegmentColumn("segment").setTokenColumn("token")
                .setTokenTypeColumn("token_type").setTimestampColumn("timestamp").setOwnerColumn("owner").build()
    }

    @Bean
    @Throws(SQLException::class)
	open fun tokenStore(dataSource: DataSource?, serializer: Serializer?): TokenStore {
        val connectionProvider: ConnectionProvider = DataSourceConnectionProvider(dataSource)
        return JdbcTokenStore.builder().connectionProvider(connectionProvider).serializer(serializer).schema(tokenSchema())
                .claimTimeout(Duration.ofSeconds(10)).contentType(ByteArray::class.java).nodeId(ManagementFactory.getRuntimeMXBean().name).build()
    }

    private fun upcasterChain(): EventUpcaster {
        return AccountOpenedUpcaster()
    }

    @Bean
	open fun eventStorageEngine(dataSource: DataSource?, platformTransactionManager: PlatformTransactionManager?): EventStorageEngine {
        val connectionProvider: ConnectionProvider = DataSourceConnectionProvider(dataSource)
        val transactionManager: TransactionManager = SpringTransactionManager(platformTransactionManager)
        return JdbcEventStorageEngine.builder().upcasterChain(upcasterChain()).connectionProvider(connectionProvider)
                .transactionManager(transactionManager).schema(eventSchema(EVENT_TABLE_VERSION))
                .dataType(ByteArray::class.java).build()
    }

    companion object {
        private const val EVENT_TABLE_VERSION = 1
    }
}
