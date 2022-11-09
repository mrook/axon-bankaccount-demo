package org.demo.domain;

import org.axonframework.serialization.Revision;

@Revision("1.0")
public record AccountOpened(String accountId, String accountNumber) {
}
