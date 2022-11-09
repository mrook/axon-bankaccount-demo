package org.demo.domain;

public class AccountAlreadyOpenedException extends Exception {
	public AccountAlreadyOpenedException(String accountId) {
		super(String.format("Account [%s] already opened", accountId));
	}
}
