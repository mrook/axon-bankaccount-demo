package org.demo.domain

import org.axonframework.test.aggregate.AggregateTestFixture
import org.demo.domain.BankAccountEvents.ACCOUNT_ID
import org.demo.domain.BankAccountEvents.ACCOUNT_NUMBER
import org.demo.domain.BankAccountEvents.DEPOSIT_AMOUNT
import org.demo.domain.BankAccountEvents.WITHDRAW_AMOUNT
import org.demo.domain.BankAccountEvents.WITHDRAW_DEPOSIT_INSTANT
import org.demo.domain.BankAccountEvents.accountOpened
import org.demo.domain.BankAccountEvents.moneyDeposited
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountTest {
	private lateinit var fixture: AggregateTestFixture<BankAccount>

	@BeforeEach
	fun createFixture() {
		fixture = AggregateTestFixture(BankAccount::class.java)
	}

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

	private val ACCOUNT_ID = "accountId"
	private val ACCOUNT_NUMBER = "3856625"
}
