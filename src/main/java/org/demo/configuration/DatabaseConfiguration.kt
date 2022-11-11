package org.demo.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
open class DatabaseConfiguration {
    @Bean
	open fun dataSource(@Value("\${jdbc.host}") host: String, @Value("\${jdbc.port}") port: Int, @Value("\${jdbc.database}") database: String,
						@Value("\${jdbc.username}") username: String, @Value("\${jdbc.password}") password: String): DataSource {
        return DriverManagerDataSource(String.format("jdbc:postgresql://%s:%d/%s", host, port, database), username, password)
    }

    @Bean
	open fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}
