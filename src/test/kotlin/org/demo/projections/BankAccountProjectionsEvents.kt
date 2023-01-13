package org.demo.projections

import org.demo.domain.BankAccountEvents
import java.math.BigDecimal

object BankAccountProjectionsEvents {
    fun emptyBankAccount(): BankAccount {
        return BankAccount(BankAccountEvents.ACCOUNT_ID, BankAccountEvents.ACCOUNT_NUMBER, BigDecimal.ZERO)
    }
}
