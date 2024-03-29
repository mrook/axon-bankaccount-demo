package org.demo.api

import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.EventProcessor
import org.axonframework.eventhandling.EventTrackerStatus
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
class RebuildStatusController @Autowired constructor(private val eventProcessingConfiguration: EventProcessingConfiguration) {
	@GetMapping("rebuild-status")
	fun rebuildStatus(): Map<String, Map<Int, EventTrackerStatus>> {
		val eventProcessors = eventProcessingConfiguration.eventProcessors()
		return eventProcessors.values.stream()
				.filter { processor: EventProcessor -> processor is TrackingEventProcessor }
				.collect(Collectors.toMap({ obj: EventProcessor -> obj.name },
						{ processor: EventProcessor ->
							(processor as TrackingEventProcessor).processingStatus()
						}
				))
	}

	@PostMapping("start-rebuild")
	fun startRebuild() {
		eventProcessingConfiguration.eventProcessors().values.stream()
				.filter { processor: EventProcessor -> processor is TrackingEventProcessor }
				.forEach { processor ->
					val trackingEventProcessor = processor as TrackingEventProcessor
					trackingEventProcessor.shutDown()
					trackingEventProcessor.resetTokens()
					trackingEventProcessor.start()
				}
	}
}
