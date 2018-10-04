package br.com.cursomc.sbinc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.cursomc.sbinc.services.DBService;

@Configuration
@Profile("test")
public class ProfileTestConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws Exception {
		dbService.instantiateDbTest();
		return true;
	}
}
