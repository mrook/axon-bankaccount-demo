package org.demo.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record MoneyDeposited(String accountId, BigDecimal amount, ZonedDateTime timestamp) {
}
