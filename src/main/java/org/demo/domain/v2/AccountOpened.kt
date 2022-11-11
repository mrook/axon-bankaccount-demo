package org.demo.domain.v2

import org.axonframework.serialization.Revision

@Revision("2.0")
data class AccountOpened(val accountId: String, val accountNumberIban: String)
