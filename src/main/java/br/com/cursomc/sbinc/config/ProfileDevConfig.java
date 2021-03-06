package br.com.cursomc.sbinc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.cursomc.sbinc.services.DBService;
import br.com.cursomc.sbinc.services.EmailService;
import br.com.cursomc.sbinc.services.SmptEmailService;

@Configuration
@Profile("dev")
public class ProfileDevConfig {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws Exception {
		
		if (!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateDbTest();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmptEmailService();
	}
}
