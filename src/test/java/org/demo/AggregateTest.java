package org.demo;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.ParameterizedType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AggregateTest<T> {
	protected FixtureConfiguration<T> fixture;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void createFixture() {
		fixture = new AggregateTestFixture<>((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
}
