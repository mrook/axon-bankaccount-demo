package org.demo.domain

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.GenericEventMessage
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal

@Aggregate
class BankAccount() {
	@AggregateIdentifier
	private lateinit var accountId: String
	private lateinit var accountNumber: String
	private lateinit var balance: BigDecimal
	private var accountStatus: AccountStatus = AccountStatus.NEW

	enum class AccountStatus {
		NEW,
		OPENED,
		CLOSED
	}

	@CommandHandler
	@CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
	@Throws(AccountAlreadyOpenedException::class)
	fun openAccount(command: OpenAccount) {
		if (accountStatus != AccountStatus.NEW) {
			throw AccountAlreadyOpenedException(accountId)
		}
		apply(AccountOpened(command.accountId, command.accountNumber))
	}

	@EventHandler
	fun accountOpened(event: AccountOpened) {
		accountStatus = AccountStatus.OPENED
		accountId = event.accountId
		accountNumber = event.accountNumber
		balance = BigDecimal.valueOf(0)
	}

	@CommandHandler
	fun closeAccount(command: CloseAccount) {
		apply(AccountClosed(command.accountId))
	}

	@EventHandler
	fun accountClosed(event: AccountClosed) {
		accountStatus = AccountStatus.CLOSED;
	}

	@CommandHandler
	fun depositMoney(command: DepositMoney) {
		apply(MoneyDeposited(
				command.accountId,
				command.amount,
				GenericEventMessage.clock.instant()))
	}

	@EventHandler
	fun moneyDeposited(event: MoneyDeposited) {
		balance = balance.add(event.amount)
	}

	@CommandHandler
	@Throws(OverdraftDetectedException::class)
	fun withdrawMoney(command: WithdrawMoney) {
		if (balance.compareTo(command.amount) >= 0) {
			apply(MoneyWithdrawn(
					command.accountId,
					command.amount,
					GenericEventMessage.clock.instant()))
		} else {
			throw OverdraftDetectedException(accountNumber, balance, command.amount)
		}
	}

	@EventHandler
	fun moneyWithdrawn(event: MoneyWithdrawn) {
		balance = balance.subtract(event.amount)
	}
}
