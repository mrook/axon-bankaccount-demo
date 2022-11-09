package org.demo.domain;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record OpenAccount(@TargetAggregateIdentifier String accountId, String accountNumber) {
}
