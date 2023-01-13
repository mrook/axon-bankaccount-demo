package org.demo.domain

import java.math.BigDecimal
import java.time.Instant

class MoneyWithdrawn(val accountId: String, val amount: BigDecimal, val instant: Instant)
