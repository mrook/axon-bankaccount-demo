package org.demo.domain

import org.axonframework.serialization.Revision

@Revision("1.0")
data class AccountOpened(
		val accountId: String,
		val accountNumber: String
)
