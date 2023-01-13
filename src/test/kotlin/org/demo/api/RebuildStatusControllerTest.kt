package org.demo.api

import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [RebuildStatusController::class])
class RebuildStatusControllerTest {
    @MockBean
    private lateinit var eventProcessingConfiguration: EventProcessingConfiguration

    @Mock
    private lateinit var processor: TrackingEventProcessor

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun shouldGetRebuildStatusFromAProcessor() {
        `when`(processor.name).thenReturn("PROCESSOR")
        `when`(eventProcessingConfiguration.eventProcessors()).thenReturn(mapOf(Pair("", processor)))
		mockMvc
				.perform(MockMvcRequestBuilders.get("/rebuild-status"))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.content().json("{}"))
        verify(processor).name
        verify(processor).processingStatus()
    }
}
