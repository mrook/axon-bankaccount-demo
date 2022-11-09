package org.demo.domain;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public record DepositMoney(@TargetAggregateIdentifier String accountId, BigDecimal amount) {
}
