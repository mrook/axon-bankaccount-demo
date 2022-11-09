package org.demo.projections;

import org.demo.ProjectionsTest;
import org.demo.domain.BankAccountEvents;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.demo.domain.BankAccountEvents.accountClosed;
import static org.demo.domain.BankAccountEvents.accountOpened;

public class BankAccountProjectionsTest extends ProjectionsTest<BankAccountProjections> {
	@Test
	public void shouldFindOpenedBankAccountByAccountId() {
		publish(accountOpened());

		assertThat(projections.findAccountById(BankAccountEvents.ACCOUNT_ID)).isPresent();
	}

	@Test
	public void shouldNotFindUnregisteredPersonByPersonId() {
		assertThat(projections.findAccountById(BankAccountEvents.ACCOUNT_ID)).isNotPresent();
	}

	@Test
	public void shouldNotFindBankAccountAfterClosed() {
		publish(accountOpened(), accountClosed());

		assertThat(projections.findAccountById(BankAccountEvents.ACCOUNT_ID)).isNotPresent();
	}
}

