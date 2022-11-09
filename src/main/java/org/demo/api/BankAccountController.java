package org.demo.api;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.demo.projections.BankAccount;
import org.demo.projections.BankAccountProjections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class BankAccountController {
	private final CommandGateway commandGateway;
	private final IdentifierFactory identifierFactory;

	private final BankAccountProjections bankAccountProjections;

	@Autowired
	public BankAccountController(CommandGateway commandGateway, IdentifierFactory identifierFactory, BankAccountProjections bankAccountProjections) {
		this.commandGateway = commandGateway;
		this.identifierFactory = identifierFactory;

		this.bankAccountProjections = bankAccountProjections;
	}

	@GetMapping("accounts/{accountId}")
	public Optional<String> getAccountNumber(@PathVariable("accountId") String accountId) {
		return bankAccountProjections.findAccountById(accountId).map(BankAccount::accountNumber);
	}
}
