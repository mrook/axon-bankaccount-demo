package org.demo.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class OpenAccount(@TargetAggregateIdentifier val accountId: String, val accountNumber: String)
