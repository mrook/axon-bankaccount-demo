package org.demo.projections;

import java.math.BigDecimal;

public record BankAccount(String accountId, String accountNumber, BigDecimal balance) {
}
