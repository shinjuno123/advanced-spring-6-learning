package com.eazybytes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
// Optional! IF you are in a scenario where you have your controller classes outside the spring boot main class!
// @ComponentScan("com.eazybytes")
// @EnableJpaRepositories("com.eazybytes.repository")
// @EntityScan("com.eazybites.model")
// @EnableWebSecurity
public class EasyBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyBankBackendApplication.class, args);
	}

}
