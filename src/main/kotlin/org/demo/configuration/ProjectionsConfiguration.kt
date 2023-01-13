package org.demo.configuration

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.config.ProcessingGroup
import org.demo.projections.RebuildableProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils

@Configuration
class ProjectionsConfiguration {
	@Autowired
	fun startTrackingProjections(configurer: EventProcessingConfigurer) {
		val scanner = ClassPathScanningCandidateComponentProvider(false)
		scanner.addIncludeFilter(AnnotationTypeFilter(RebuildableProjection::class.java))
		for (bd in scanner.findCandidateComponents("org.demo")) {
			val projectionClass = Class.forName(bd.beanClassName)
			val rebuildableProjection = projectionClass.getAnnotation(RebuildableProjection::class.java)
			if (rebuildableProjection.rebuild) {
				registerRebuildableProjection(configurer, projectionClass, rebuildableProjection)
			}
		}
	}

	private fun registerRebuildableProjection(configurer: EventProcessingConfigurer,
											  projectionClass: Class<*>,
											  rebuildableProjection: RebuildableProjection) {
		val processingGroup = projectionClass.getAnnotation(ProcessingGroup::class.java)
		val name = processingGroup?.let(ProcessingGroup::value)
				?: (projectionClass.name + "/" + rebuildableProjection.version)

		configurer.assignHandlerTypesMatching(name) { eventHandler ->
			projectionClass.isAssignableFrom(ClassUtils.getUserClass(eventHandler))
		}

		configurer.registerTrackingEventProcessor(name)
	}
}
