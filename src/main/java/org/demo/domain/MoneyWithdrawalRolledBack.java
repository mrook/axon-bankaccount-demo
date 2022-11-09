package org.demo.domain;

import java.math.BigDecimal;

public record MoneyWithdrawalRolledBack(String accountId, BigDecimal amount) {
}
