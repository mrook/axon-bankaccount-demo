package org.demo.domain.gdpr;

import lombok.Value;

@Value
public class AccountOpened {
	String accountId;

	String accountNumber;

	String fullName;
}
