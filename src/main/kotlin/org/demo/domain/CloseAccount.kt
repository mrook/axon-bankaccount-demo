package org.demo.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CloseAccount(@TargetAggregateIdentifier val accountId: String)
