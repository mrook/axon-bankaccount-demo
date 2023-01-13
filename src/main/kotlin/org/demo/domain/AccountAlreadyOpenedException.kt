package org.demo.domain

class AccountAlreadyOpenedException(accountId: String?) : Exception(String.format("Account [%s] already opened", accountId))
