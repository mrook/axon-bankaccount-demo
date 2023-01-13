package org.demo.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal

data class DepositMoney(
		@TargetAggregateIdentifier val accountId: String,
		val amount: BigDecimal
)
