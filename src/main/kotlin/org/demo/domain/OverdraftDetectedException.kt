package org.demo.domain

import java.math.BigDecimal

class OverdraftDetectedException(accountNumber: String?, balance: BigDecimal?, withdrawalAmount: BigDecimal?) : Exception(String.format("Overdraft detected on account %s [current balance: %.2f, requested amount: %.2f]",
        accountNumber, balance, withdrawalAmount))
