package org.demo.upcasters;

import com.thoughtworks.xstream.XStream;
import org.axonframework.eventhandling.EventData;
import org.axonframework.eventhandling.GenericDomainEventEntry;
import org.axonframework.eventhandling.GlobalSequenceTrackingToken;
import org.axonframework.eventhandling.TrackedDomainEventData;
import org.axonframework.messaging.MetaData;
import org.axonframework.serialization.SerializedObject;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.SimpleSerializedObject;
import org.axonframework.serialization.SimpleSerializedType;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.upcasting.event.InitialEventRepresentation;
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation;
import org.demo.domain.BankAccountEvents;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AccountOpenedUpcasterTest {
	@Test
	public void shouldUpcastOldPersonRegisteredEvent() {
		XStream xStream = new XStream();
		xStream.allowTypesByWildcard(new String[]{"org.demo.**"});
		Serializer serializer = JacksonSerializer.defaultSerializer();

		String serializedData = String.format("{\"accountId\":\"%s\",\"accountNumber\":\"%s\"}",
				BankAccountEvents.ACCOUNT_ID, BankAccountEvents.ACCOUNT_NUMBER);
		SerializedObject<String> serializedPayload = new SimpleSerializedObject<>(serializedData, String.class,
				new SimpleSerializedType(org.demo.domain.AccountOpened.class.getTypeName(), "1.0"));

		EventData<?> eventData = new TrackedDomainEventData<Object>(new GlobalSequenceTrackingToken(10), new GenericDomainEventEntry<>("test", "aggregateId", 0, "eventId", Instant.now(),
				serializedPayload.getType().getName(), serializedPayload.getType().getRevision(), serializedData,
				serializer.serialize(MetaData.emptyInstance(), String.class)));

		AccountOpenedUpcaster upcaster = new AccountOpenedUpcaster();

		List<IntermediateEventRepresentation> result = upcaster.upcast(Stream.of(new InitialEventRepresentation(eventData, serializer))).toList();

		assertFalse(result.isEmpty());

		IntermediateEventRepresentation firstEvent = result.get(0);
		assertEquals("2.0", firstEvent.getType().getRevision());
		org.demo.domain.v2.AccountOpened upcastedEvent = serializer.deserialize(firstEvent.getData());
		assertEquals(BankAccountEvents.ACCOUNT_IBAN, upcastedEvent.accountNumberIban());
		assertEquals(eventData.getEventIdentifier(), firstEvent.getMessageIdentifier());
		assertEquals(eventData.getTimestamp(), firstEvent.getTimestamp());
	}
}
