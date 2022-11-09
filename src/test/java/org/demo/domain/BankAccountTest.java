package org.demo.domain;

import org.demo.AggregateTest;
import org.junit.jupiter.api.Test;

import static org.demo.domain.BankAccountEvents.*;

public class BankAccountTest extends AggregateTest<BankAccount> {
	@Test
	public void canOpenAccount() {
		fixture.givenNoPriorActivity()
				.when(new OpenAccount(ACCOUNT_ID, ACCOUNT_NUMBER))
				.expectEvents(accountOpened());
	}

	@Test
	public void cannotOpenSameAccountTwice() {
		fixture.given(accountOpened())
				.when(new OpenAccount(ACCOUNT_ID, ACCOUNT_NUMBER))
				.expectException(AccountAlreadyOpenedException.class);
	}

	@Test
	public void noOverdraftsOnEmptyAccount() {
		fixture.given(accountOpened())
			.when(new WithdrawMoney(ACCOUNT_ID, WITHDRAW_AMOUNT))
			.expectException(OverdraftDetectedException.class);
	}

	@Test
	public void newAccountHasZeroBalance() {
		fixture.given(accountOpened())
				.andGivenCurrentTime(WITHDRAW_DEPOSIT_INSTANT)
				.when(new DepositMoney(ACCOUNT_ID, DEPOSIT_AMOUNT))
				.expectEvents(moneyDeposited());
	}
}
