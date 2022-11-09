package org.demo.domain;

import java.math.BigDecimal;

public record MoneyWithdrawn(String accountId, BigDecimal amount, java.time.Instant timestamp) {
}
