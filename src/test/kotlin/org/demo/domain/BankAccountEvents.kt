package org.demo.domain

import java.math.BigDecimal
import java.time.Instant

object BankAccountEvents {
    @JvmStatic
	fun accountOpened(): AccountOpened {
        return AccountOpened(ACCOUNT_ID, ACCOUNT_NUMBER)
    }

    @JvmStatic
	fun accountClosed(): AccountClosed {
        return AccountClosed(ACCOUNT_ID)
    }

	@JvmStatic
    fun moneyWithdrawn(): MoneyWithdrawn {
        return MoneyWithdrawn(ACCOUNT_ID, WITHDRAW_AMOUNT, WITHDRAW_DEPOSIT_INSTANT)
    }

    @JvmStatic
	fun moneyDeposited(): MoneyDeposited {
        return MoneyDeposited(ACCOUNT_ID, DEPOSIT_AMOUNT, WITHDRAW_DEPOSIT_INSTANT)
    }

    const val ACCOUNT_ID = "accountId"
    const val ACCOUNT_NUMBER = "3856625"
    const val ACCOUNT_IBAN = "NL98INGB0003856625"
    @JvmField
	val WITHDRAW_AMOUNT = BigDecimal.valueOf(567)
    @JvmField
	val DEPOSIT_AMOUNT = BigDecimal.valueOf(1234)
    @JvmField
	val WITHDRAW_DEPOSIT_INSTANT = Instant.now()
}
