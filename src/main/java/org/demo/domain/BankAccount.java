package org.demo.domain;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@NoArgsConstructor
public class BankAccount {
	@AggregateIdentifier
	private String accountId;
	private String accountNumber;
	private BigDecimal balance;

	@CommandHandler
	public BankAccount(OpenAccount command) {
		apply(new AccountOpened(command.accountId(), command.accountNumber()));
	}

	@EventHandler
	public void accountOpened(AccountOpened event) {
		this.accountId = event.accountId();
		this.accountNumber = event.accountNumber();
		this.balance = BigDecimal.valueOf(0);
	}

	@CommandHandler
	public void depositMoney(DepositMoney command) {
		apply(new MoneyDeposited(
				command.accountId(),
				command.amount(),
				ZonedDateTime.now()));
	}

	@EventHandler
	public void moneyDeposited(MoneyDeposited event) {
		this.balance = this.balance.add(event.amount());
	}

	@CommandHandler
	public void withdrawMoney(WithdrawMoney command) throws OverdraftDetectedException {
		if (balance.compareTo(command.amount()) >= 0) {
			apply(new MoneyWithdrawn(
					command.accountId(),
					command.amount(),
					ZonedDateTime.now()));
		} else {
			throw new OverdraftDetectedException(accountNumber, balance, command.amount());
		}
	}

	@EventHandler
	public void moneyWithdrawn(MoneyWithdrawn event) {
		this.balance = this.balance.subtract(event.amount());
	}
}
