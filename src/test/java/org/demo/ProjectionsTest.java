package org.demo;

import org.axonframework.eventhandling.*;
import org.demo.configuration.TestProjectionsConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestProjectionsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ProjectionsTest<P> {
	protected P projections;

	private EventBus eventBus;

	private SubscribingEventProcessor eventProcessor;

	@BeforeEach
	public void createEventBus() {
		eventBus = SimpleEventBus.builder().build();

		eventProcessor = SubscribingEventProcessor.builder().name("listener")
				.eventHandlerInvoker(SimpleEventHandlerInvoker.builder().eventHandlers(projections).build())
				.messageSource(eventBus)
				.build();

		eventProcessor.start();
	}

	@AfterEach
	public void stopEventListener() {
		eventProcessor.shutDown();
	}

	@Autowired
	public void setProjections(P projections) {
		this.projections = projections;
	}

	protected void publish(Object... events) {
		publishAt(null, events);
	}

	protected void publishAt(Instant instant, Object... events) {
		Clock previousClock = GenericEventMessage.clock;

		try {
			GenericEventMessage.clock = (instant == null) ? previousClock : Clock.fixed(instant, ZoneId.of("UTC"));

			Arrays.stream(events).forEach(event -> eventBus.publish(GenericEventMessage.asEventMessage(event)));
		} finally {
			GenericEventMessage.clock = previousClock;
		}
	}

	protected void publishAndRefresh(Object... events) {
		publishAndRefreshAt(null, events);
	}

	protected void publishAndRefreshAt(Instant instant, Object... events) {
		publishAt(instant, events);
	}

}
