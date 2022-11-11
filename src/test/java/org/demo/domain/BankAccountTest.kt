package org.demo.domain

import org.demo.AggregateTest
import org.demo.domain.BankAccountEvents.ACCOUNT_ID
import org.demo.domain.BankAccountEvents.ACCOUNT_NUMBER
import org.demo.domain.BankAccountEvents.DEPOSIT_AMOUNT
import org.demo.domain.BankAccountEvents.WITHDRAW_AMOUNT
import org.demo.domain.BankAccountEvents.WITHDRAW_DEPOSIT_INSTANT
import org.demo.domain.BankAccountEvents.accountOpened
import org.demo.domain.BankAccountEvents.moneyDeposited
import org.junit.jupiter.api.Test

class BankAccountTest : AggregateTest<BankAccount>(BankAccount::class) {
    @Test
    fun canOpenAccount() {
        fixture.givenNoPriorActivity()
                .`when`(OpenAccount(ACCOUNT_ID, ACCOUNT_NUMBER))
                .expectEvents(accountOpened())
    }

    @Test
    fun cannotOpenSameAccountTwice() {
        fixture.given(accountOpened())
                .`when`(OpenAccount(ACCOUNT_ID, ACCOUNT_NUMBER))
                .expectException(AccountAlreadyOpenedException::class.java)
    }

    @Test
    fun noOverdraftsOnEmptyAccount() {
        fixture.given(accountOpened())
                .`when`(WithdrawMoney(ACCOUNT_ID, WITHDRAW_AMOUNT))
                .expectException(OverdraftDetectedException::class.java)
    }

    @Test
    fun newAccountHasZeroBalance() {
        fixture.given(accountOpened())
                .andGivenCurrentTime(WITHDRAW_DEPOSIT_INSTANT)
                .`when`(DepositMoney(ACCOUNT_ID, DEPOSIT_AMOUNT))
                .expectEvents(moneyDeposited())
    }
}
