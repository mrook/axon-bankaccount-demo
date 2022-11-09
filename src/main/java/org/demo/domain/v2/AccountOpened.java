package org.demo.domain.v2;

import org.axonframework.serialization.Revision;

@Revision("2.0")
public record AccountOpened(String accountId, String accountNumberIban) {
}
