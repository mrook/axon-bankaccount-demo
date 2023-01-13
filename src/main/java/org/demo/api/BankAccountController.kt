package org.demo.api

import org.demo.projections.BankAccount
import org.demo.projections.BankAccountProjections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BankAccountController @Autowired constructor(
		private val bankAccountProjections: BankAccountProjections) {
	@GetMapping("accounts/{accountId}")
	fun getAccountNumber(@PathVariable("accountId") accountId: String?):
			Optional<String> {
		return bankAccountProjections.findAccountById(accountId!!)
				.map { obj: BankAccount -> obj.accountNumber }
	}

	@GetMapping("accounts/active")
	fun getNumberOfAccounts(): Int {
		return bankAccountProjections.getNumberOfAccounts()
	}
}
