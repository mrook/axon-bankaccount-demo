package org.demo.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

@Configuration
@ComponentScan(basePackages = "org.demo", includeFilters = @ComponentScan.Filter(Repository.class), useDefaultFilters = false)
public class TestProjectionsConfiguration {
	@Bean
	public Gson gson() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
	}
}
