package org.demo.domain;

import java.math.BigDecimal;
import java.time.Instant;

public class BankAccountEvents {
	public static AccountOpened accountOpened() {
		return new AccountOpened(ACCOUNT_ID, ACCOUNT_NUMBER);
	}
	public static AccountClosed accountClosed() {
		return new AccountClosed(ACCOUNT_ID);
	}
	public static MoneyWithdrawn moneyWithdrawn() { return new MoneyWithdrawn(ACCOUNT_ID, WITHDRAW_AMOUNT, WITHDRAW_DEPOSIT_INSTANT); }
	public static MoneyDeposited moneyDeposited() {
		return new MoneyDeposited(ACCOUNT_ID, DEPOSIT_AMOUNT, WITHDRAW_DEPOSIT_INSTANT);
	}

	public final static String ACCOUNT_ID = "accountId";
	public final static String ACCOUNT_NUMBER = "3856625";
	public final static String ACCOUNT_IBAN = "NL98INGB0003856625";
	public final static BigDecimal WITHDRAW_AMOUNT = BigDecimal.valueOf(567);
	public final static BigDecimal DEPOSIT_AMOUNT = BigDecimal.valueOf(1234);
	public final static Instant WITHDRAW_DEPOSIT_INSTANT = Instant.now();
}
