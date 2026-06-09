package br.com.rml.BFF_PROJECT;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableFeignClients
public class Application implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		String activeProfile = Arrays.toString(environment.getActiveProfiles());
		String profiles = defaultIfBlank(activeProfile.replace("[]", ""), "[DEFAULT]");
		LOGGER.info("ACTIVE PROFILES: {}", profiles);
		LOGGER.info("BFF iniciado com sucesso.");
	}
}
