package org.demo.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {
    @Bean
	fun dataSource(@Value("\${jdbc.host}") host: String, @Value("\${jdbc.port}") port: Int, @Value("\${jdbc.username}") username: String,
				   @Value("\${jdbc.database}") database: String, @Value("\${jdbc.password}") password: String): DataSource {
        return DriverManagerDataSource(String.format("jdbc:postgresql://%s:%d/%s", host, port, database), username, password)
    }

    @Bean
	fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}
