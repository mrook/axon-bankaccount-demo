package org.demo.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal

data class WithdrawMoney(@TargetAggregateIdentifier val accountId: String, val amount: BigDecimal)
