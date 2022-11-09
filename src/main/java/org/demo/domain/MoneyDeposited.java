package org.demo.domain;

import java.math.BigDecimal;

public record MoneyDeposited(String accountId, BigDecimal amount, java.time.Instant timestamp) {
}
