package org.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoService

fun main(parameters: Array<String>) {
	runApplication<DemoService>(*parameters)
}
