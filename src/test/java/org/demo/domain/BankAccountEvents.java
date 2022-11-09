package org.demo.domain;

public class BankAccountEvents {
	public static AccountOpened accountOpened() {
		return new AccountOpened(ACCOUNT_ID, ACCOUNT_NUMBER);
	}

	public static AccountClosed accountClosed() {
		return new AccountClosed(ACCOUNT_ID);
	}

	public final static String ACCOUNT_ID = "accountId";
	public final static String ACCOUNT_NUMBER = "3856625";

	public final static String ACCOUNT_IBAN = "NL98INGB0003856625";
}
