package org.demo

import org.axonframework.eventhandling.*
import org.demo.configuration.TestProjectionsConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestProjectionsConfiguration::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class ProjectionsTest<P: Any> {
	@Autowired
    protected lateinit var projections: P

    private var eventBus: EventBus? = null
    private var eventProcessor: SubscribingEventProcessor? = null
    @BeforeEach
    fun createEventBus() {
        eventBus = SimpleEventBus.builder().build()
        eventProcessor = SubscribingEventProcessor.builder().name("listener")
                .eventHandlerInvoker(SimpleEventHandlerInvoker.builder<SimpleEventHandlerInvoker.Builder<*>>().eventHandlers(projections).build())
                .messageSource(eventBus!!)
                .build()
        eventProcessor!!.start()
    }

    @AfterEach
    fun stopEventListener() {
        eventProcessor!!.shutDown()
    }

    protected fun publish(vararg events: Any) {
        publishAt(null, *events)
    }

    protected fun publishAt(instant: Instant?, vararg events: Any) {
        val previousClock = GenericEventMessage.clock
        try {
            GenericEventMessage.clock = if (instant == null) previousClock else Clock.fixed(instant, ZoneId.of("UTC"))
            Arrays.stream(events).forEach { event: Any? -> eventBus!!.publish(GenericEventMessage.asEventMessage<Any>(event!!)) }
        } finally {
            GenericEventMessage.clock = previousClock
        }
    }

    protected fun publishAndRefresh(vararg events: Any) {
        publishAndRefreshAt(null, *events)
    }

    protected fun publishAndRefreshAt(instant: Instant?, vararg events: Any) {
        publishAt(instant, *events)
    }
}
