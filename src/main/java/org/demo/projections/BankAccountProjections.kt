package org.demo.projections

import org.axonframework.eventhandling.EventHandler
import org.demo.domain.AccountClosed
import org.demo.domain.AccountOpened
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
open class BankAccountProjections {
    private val activeAccounts: MutableMap<String, BankAccount> = HashMap()
    @EventHandler
    fun onAccountOpened(accountOpened: AccountOpened) {
        if (findAccountById(accountOpened.accountId).isPresent) {
            throw RuntimeException("This bank account should not be present, but it is")
        }
        val bankAccount = BankAccount(accountOpened.accountId, accountOpened.accountNumber, BigDecimal.ZERO)
        activeAccounts[accountOpened.accountId] = bankAccount
    }

    @EventHandler
    fun onAccountClosed(accountClosed: AccountClosed) {
        if (findAccountById(accountClosed.accountId).isEmpty) {
            throw RuntimeException("This bank account should be present, but it isn't")
        }
        activeAccounts.remove(accountClosed.accountId)
    }

    fun findAccountById(accountId: String): Optional<BankAccount> {
        return Optional.ofNullable(activeAccounts[accountId])
    }
}
