package org.demo.projections

import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.demo.domain.AccountClosed
import org.demo.domain.AccountOpened
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@Component
@RebuildableProjection(version = "1", rebuild = true)
class BankAccountProjections {
	private val logger = LoggerFactory.getLogger(BankAccountProjections::class.java)
    private val activeAccounts: MutableMap<String, BankAccount> = HashMap()

	@ResetHandler
	fun resetProjections() {
		logger.info(::resetProjections.name)
		activeAccounts.clear()
	}

    @EventHandler
    fun onAccountOpened(accountOpened: AccountOpened) {
		logger.info(::onAccountOpened.name)
        if (findAccountById(accountOpened.accountId).isPresent) {
            throw ProjectionsLogicException("This bank account should not be present, but it is")
        }
        val bankAccount = BankAccount(
				accountOpened.accountId,
				accountOpened.accountNumber,
				BigDecimal.ZERO)
        activeAccounts[accountOpened.accountId] = bankAccount
    }

    @EventHandler
    fun onAccountClosed(accountClosed: AccountClosed) {
		logger.info(::onAccountClosed.name)
        if (findAccountById(accountClosed.accountId).isEmpty) {
            throw ProjectionsLogicException("This bank account should be present, but it isn't")
        }
        activeAccounts.remove(accountClosed.accountId)
    }

    fun findAccountById(accountId: String): Optional<BankAccount> {
        return Optional.ofNullable(activeAccounts[accountId])
    }

	fun getNumberOfAccounts(): Int {
		return activeAccounts.size
	}
}
