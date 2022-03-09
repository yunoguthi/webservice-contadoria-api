package br.jus.jfsp.nuit.contadoria;
//import org.springframework.context.annotation.Configuration;
import br.jus.jfsp.nuit.predial.security.SpringSecurityAuditorAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@Configuration
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef="auditorAware")
public class Application {

	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}