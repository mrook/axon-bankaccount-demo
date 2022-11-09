package org.demo.upcasters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import nl.garvelink.iban.IBAN;
import org.axonframework.serialization.SerializedType;
import org.axonframework.serialization.SimpleSerializedType;
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation;
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster;

@Getter
public class AccountOpenedUpcaster extends SingleEventUpcaster {
	private final SerializedType typeConsumed
			= new SimpleSerializedType(org.demo.domain.AccountOpened.class.getTypeName(), "1.0");
	private final SerializedType typeProduced
			= new SimpleSerializedType(org.demo.domain.v2.AccountOpened.class.getTypeName(), "2.0");

	@Override
	protected boolean canUpcast(IntermediateEventRepresentation intermediateRepresentation) {
		return intermediateRepresentation.getType().equals(typeConsumed);
	}

	@Override
	protected IntermediateEventRepresentation doUpcast(IntermediateEventRepresentation intermediateRepresentation) {
		return intermediateRepresentation.upcastPayload(getTypeProduced(), JsonNode.class, event -> {
			ObjectNode node = (ObjectNode) event;
			String accountNumber = node.get("accountNumber").asText();
			node.put("accountNumberIban", toIban(accountNumber));
			node.remove("accountNumber");
			return event;
		});
	}

	private String toIban(String accountNumber) {
		return IBAN.compose("NL", "INGB000" + accountNumber).toPlainString();
	}
}
