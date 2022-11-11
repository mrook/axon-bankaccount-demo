package org.demo.projections

import org.assertj.core.api.Assertions
import org.demo.ProjectionsTest
import org.demo.domain.BankAccountEvents
import org.demo.domain.BankAccountEvents.accountClosed
import org.demo.domain.BankAccountEvents.accountOpened
import org.junit.jupiter.api.Test

class BankAccountProjectionsTest : ProjectionsTest<BankAccountProjections>() {
    @Test
    fun shouldFindOpenedBankAccountByAccountId() {
        publish(accountOpened())
        Assertions.assertThat(projections.findAccountById(BankAccountEvents.ACCOUNT_ID)).isPresent
    }

    @Test
    fun shouldNotFindUnregisteredPersonByPersonId() {
        Assertions.assertThat(projections.findAccountById(BankAccountEvents.ACCOUNT_ID)).isNotPresent
    }

    @Test
    fun shouldNotFindBankAccountAfterClosed() {
        publish(accountOpened(), accountClosed())
        Assertions.assertThat(projections.findAccountById(BankAccountEvents.ACCOUNT_ID)).isNotPresent
    }
}
