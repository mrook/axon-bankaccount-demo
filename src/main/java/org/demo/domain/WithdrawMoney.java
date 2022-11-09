package org.demo.domain;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public record WithdrawMoney(@TargetAggregateIdentifier String accountId, BigDecimal amount) {
}
