package org.demo.projections;

import org.demo.domain.BankAccountEvents;

import java.math.BigDecimal;

public class BankAccountProjectionsEvents {
	public static BankAccount emptyBankAccount() {
		return new BankAccount(BankAccountEvents.ACCOUNT_ID, BankAccountEvents.ACCOUNT_NUMBER, BigDecimal.ZERO);
	}
}
