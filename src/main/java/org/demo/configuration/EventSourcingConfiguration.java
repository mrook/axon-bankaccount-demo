package org.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.jdbc.DataSourceConnectionProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore;
import org.axonframework.eventhandling.tokenstore.jdbc.TokenSchema;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.demo.upcasters.AccountOpenedUpcaster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.time.Duration;

@Slf4j
@Configuration
public class EventSourcingConfiguration {
	private static final int EVENT_TABLE_VERSION = 1;

	@Bean
	public IdentifierFactory identifierFactory() {
		return IdentifierFactory.getInstance();
	}

	private EventSchema eventSchema(int version) {
		return EventSchema.builder().eventTable(String.format("events_%d", version)).snapshotTable("snapshot_events")
				.globalIndexColumn("id").aggregateIdentifierColumn("aggregate_id").sequenceNumberColumn("sequence_number").typeColumn("type")
				.eventIdentifierColumn("event_id").metaDataColumn("metadata").payloadColumn("payload").payloadRevisionColumn("payload_revision")
				.payloadTypeColumn("payload_type").timestampColumn("timestamp").build();
	}

	private TokenSchema tokenSchema() {
		return TokenSchema.builder().setTokenTable("tokens").setProcessorNameColumn("processor_name").setSegmentColumn("segment").setTokenColumn("token")
				.setTokenTypeColumn("token_type").setTimestampColumn("timestamp").setOwnerColumn("owner").build();
	}

	@Bean
	public TokenStore tokenStore(DataSource dataSource, Serializer serializer) throws SQLException {
		ConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource);

		return JdbcTokenStore.builder().connectionProvider(connectionProvider).serializer(serializer).schema(tokenSchema())
				.claimTimeout(Duration.ofSeconds(10)).contentType(byte[].class).nodeId(ManagementFactory.getRuntimeMXBean().getName()).build();
	}

	private EventUpcaster upcasterChain() {
		return new AccountOpenedUpcaster();
	}

	@Bean
	public EventStorageEngine eventStorageEngine(DataSource dataSource, PlatformTransactionManager platformTransactionManager) {
		ConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource);
		TransactionManager transactionManager = new SpringTransactionManager(platformTransactionManager);

		return JdbcEventStorageEngine.builder().upcasterChain(upcasterChain()).connectionProvider(connectionProvider)
				.transactionManager(transactionManager).schema(eventSchema(EVENT_TABLE_VERSION))
				.dataType(byte[].class).build();
	}
}
