package org.demo.upcasters

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.thoughtworks.xstream.XStream
import org.axonframework.eventhandling.EventData
import org.axonframework.eventhandling.GenericDomainEventEntry
import org.axonframework.eventhandling.GlobalSequenceTrackingToken
import org.axonframework.eventhandling.TrackedDomainEventData
import org.axonframework.messaging.MetaData
import org.axonframework.serialization.SerializedObject
import org.axonframework.serialization.SimpleSerializedObject
import org.axonframework.serialization.SimpleSerializedType
import org.axonframework.serialization.json.JacksonSerializer
import org.axonframework.serialization.upcasting.event.InitialEventRepresentation
import org.demo.domain.AccountOpened
import org.demo.domain.BankAccountEvents
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.stream.Stream

class AccountOpenedUpcasterTest {
	@Test
	fun shouldUpcastOldPersonRegisteredEvent() {
		val xStream = XStream()
		xStream.allowTypesByWildcard(arrayOf("org.demo.**"))
		val serializer = JacksonSerializer.defaultSerializer()
		serializer.objectMapper.registerKotlinModule()

		val serializedData = String.format("{\"accountId\":\"%s\",\"accountNumber\":\"%s\"}",
				BankAccountEvents.ACCOUNT_ID, BankAccountEvents.ACCOUNT_NUMBER)
		val serializedPayload: SerializedObject<String> = SimpleSerializedObject(serializedData, String::class.java,
				SimpleSerializedType(AccountOpened::class.java.typeName, "1.0"))

		val eventData: EventData<*> = TrackedDomainEventData(GlobalSequenceTrackingToken(10), GenericDomainEventEntry("test", "aggregateId", 0, "eventId", Instant.now(),
				serializedPayload.type.name, serializedPayload.type.revision, serializedData,
				serializer.serialize(MetaData.emptyInstance(), String::class.java)))

		val upcaster = AccountOpenedUpcaster()
		val result = upcaster.upcast(Stream.of(InitialEventRepresentation(eventData, serializer))).toList()

		assertFalse(result.isEmpty())
		val firstEvent = result[0]
		assertEquals("2.0", firstEvent.type.revision)
		val upcastedEvent: org.demo.domain.v2.AccountOpened = serializer.deserialize(firstEvent.data)
		assertEquals(BankAccountEvents.ACCOUNT_IBAN, upcastedEvent.accountNumberIban)
		assertEquals(eventData.eventIdentifier, firstEvent.messageIdentifier)
		assertEquals(eventData.timestamp, firstEvent.timestamp)
	}
}
