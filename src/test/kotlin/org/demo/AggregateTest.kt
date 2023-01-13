package org.demo

import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import kotlin.reflect.KClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class AggregateTest<T: Any>(private val clazz: KClass<T>) {
    protected lateinit var fixture: AggregateTestFixture<T>
    @BeforeEach
    fun createFixture() {
		fixture = AggregateTestFixture(clazz.java)
        fixture.setReportIllegalStateChange(false)
    }
}
