package org.demo.domain;

import java.math.BigDecimal;

public class OverdraftDetectedException extends Exception {
	public OverdraftDetectedException(String accountNumber, BigDecimal balance, BigDecimal withdrawalAmount) {
		super(String.format("Overdraft detected on account %s [current balance: %.2f, requested amount: %.2f]",
				accountNumber, balance, withdrawalAmount));
	}
}
