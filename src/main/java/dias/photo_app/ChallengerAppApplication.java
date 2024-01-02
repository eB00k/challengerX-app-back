package dias.photo_app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ChallengerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengerAppApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringAppContext springAppContext() {
		return new SpringAppContext();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
