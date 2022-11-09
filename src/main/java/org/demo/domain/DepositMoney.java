package org.demo.domain;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public record DepositMoney(@TargetAggregateIdentifier String accountId, BigDecimal amount) {
}
