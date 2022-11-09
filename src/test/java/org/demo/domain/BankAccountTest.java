package org.demo.domain;

import org.demo.AggregateTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class BankAccountTest extends AggregateTest<BankAccount> {
	@Test
	public void noOverdraftsOnEmptyAccount() {
		fixture.given(new AccountOpened(ACCOUNT_ID, ACCOUNT_NUMBER))
			.when(new WithdrawMoney(ACCOUNT_ID, new BigDecimal(20)))
			.expectException(OverdraftDetectedException.class);
	}

	private final static String ACCOUNT_ID = "accountId";
	private final static String ACCOUNT_NUMBER = "accountNumber";
}
