package org.demo.projections;

import org.axonframework.eventhandling.EventHandler;
import org.demo.domain.AccountClosed;
import org.demo.domain.AccountOpened;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BankAccountProjections {
	private final Map<String, BankAccount> activeAccounts = new HashMap<>();

	@EventHandler
	public void onAccountOpened(AccountOpened accountOpened) {
		if (findAccountById(accountOpened.accountId()).isPresent()) {
			throw new RuntimeException("This bank account should not be present, but it is");
		}
		BankAccount bankAccount = new BankAccount(accountOpened.accountId(), accountOpened.accountNumber(), BigDecimal.ZERO);
		activeAccounts.put(accountOpened.accountId(), bankAccount);
	}

	@EventHandler
	public void onAccountClosed(AccountClosed accountClosed) {
		if (findAccountById(accountClosed.accountId()).isEmpty()) {
			throw new RuntimeException("This bank account should be present, but it isn't");
		}
		activeAccounts.remove(accountClosed.accountId());
	}

	public Optional<BankAccount> findAccountById(String accountId) {
		return Optional.ofNullable(activeAccounts.get(accountId));
	}
}
