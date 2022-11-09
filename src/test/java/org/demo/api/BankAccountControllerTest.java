package org.demo.api;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.demo.domain.BankAccountEvents;
import org.demo.projections.BankAccountProjections;
import org.demo.projections.BankAccountProjectionsEvents;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {BankAccountController.class})
public class BankAccountControllerTest {
	@MockBean
	private CommandGateway commandGateway;

	@MockBean
	private IdentifierFactory identifierFactory;

	@MockBean
	private BankAccountProjections bankAccountProjections;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnBankAccount() throws Exception {
		when(bankAccountProjections.findAccountById(BankAccountEvents.ACCOUNT_ID)).thenReturn(Optional.of(BankAccountProjectionsEvents.emptyBankAccount()));

		when(identifierFactory.generateIdentifier()).thenReturn(BankAccountEvents.ACCOUNT_ID);

		mockMvc
				.perform(get("/accounts/{accountId}", BankAccountEvents.ACCOUNT_ID))
				.andExpect(status().isOk())
				.andExpect(content().json("\"%s\"".formatted(BankAccountEvents.ACCOUNT_NUMBER)));

		verifyNoInteractions(commandGateway, identifierFactory);
	}
}
