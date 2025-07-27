package uz.akbar.web_push_backend.config;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class SecurityConfig {

	@PostConstruct
	public void setup() {

		if (Security.getProvider("BC") == null)
			Security.addProvider(new BouncyCastleProvider());

	}
}
