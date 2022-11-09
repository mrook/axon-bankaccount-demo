package org.demo.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record MoneyWithdrawn(String accountId, BigDecimal amount, ZonedDateTime timestamp) {
}
