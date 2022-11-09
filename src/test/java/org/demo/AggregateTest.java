package org.demo;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.ParameterizedType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AggregateTest<T> {
	protected FixtureConfiguration<T> fixture;

	@BeforeAll
	@SuppressWarnings("unchecked")
	public void createFixture() {
		fixture = new AggregateTestFixture<>((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		fixture.setReportIllegalStateChange(false);
	}
}
