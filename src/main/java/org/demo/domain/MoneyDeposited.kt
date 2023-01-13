package org.demo.domain

import java.math.BigDecimal
import java.time.Instant

data class MoneyDeposited(
		val accountId: String,
		val amount: BigDecimal,
		val instant: Instant
)
