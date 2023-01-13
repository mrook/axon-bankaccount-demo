package org.demo.api

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.common.IdentifierFactory
import org.demo.domain.BankAccountEvents
import org.demo.projections.BankAccountProjections
import org.demo.projections.BankAccountProjectionsEvents
import org.demo.projections.BankAccountProjectionsEvents.emptyBankAccount
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [BankAccountController::class])
class BankAccountControllerTest {
	@MockBean
	private val commandGateway: CommandGateway? = null

	@MockBean
	private val identifierFactory: IdentifierFactory? = null

	@MockBean
	private lateinit var bankAccountProjections: BankAccountProjections

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	@Throws(Exception::class)
	fun shouldReturnBankAccount() {
		`when`(bankAccountProjections.findAccountById(BankAccountEvents.ACCOUNT_ID)).thenReturn(Optional.of(emptyBankAccount()))
		`when`(identifierFactory!!.generateIdentifier()).thenReturn(BankAccountEvents.ACCOUNT_ID)
		mockMvc
				.perform(MockMvcRequestBuilders.get("/accounts/{accountId}", BankAccountEvents.ACCOUNT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.content().json("\"%s\"".formatted(BankAccountEvents.ACCOUNT_NUMBER)))
		Mockito.verifyNoInteractions(commandGateway, identifierFactory)
	}
}
