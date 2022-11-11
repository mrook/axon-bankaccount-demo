package org.demo.upcasters

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import nl.garvelink.iban.IBAN
import org.axonframework.serialization.SerializedType
import org.axonframework.serialization.SimpleSerializedType
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster
import org.demo.domain.AccountOpened

class AccountOpenedUpcaster : SingleEventUpcaster() {
    private val typeConsumed: SerializedType = SimpleSerializedType(AccountOpened::class.java.typeName, "1.0")
    private val typeProduced: SerializedType = SimpleSerializedType(org.demo.domain.v2.AccountOpened::class.java.typeName, "2.0")
    override fun canUpcast(intermediateRepresentation: IntermediateEventRepresentation): Boolean {
        return intermediateRepresentation.type == typeConsumed
    }

    override fun doUpcast(intermediateRepresentation: IntermediateEventRepresentation): IntermediateEventRepresentation {
        return intermediateRepresentation.upcastPayload(typeProduced, JsonNode::class.java) { event: JsonNode ->
            val node = event as ObjectNode
            val accountNumber = node["accountNumber"].asText()
            node.put("accountNumberIban", toIban(accountNumber))
            node.remove("accountNumber")
            event
        }
    }

    private fun toIban(accountNumber: String): String {
        return IBAN.compose("NL", "INGB000$accountNumber").toPlainString()
    }
}
