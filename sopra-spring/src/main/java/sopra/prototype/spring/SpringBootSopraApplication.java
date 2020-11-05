package sopra.prototype.spring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "es.sopra.prototype")
@EntityScan(basePackages = "es.sopra.prototype")
@EnableJpaRepositories(basePackages = "es.sopra.prototype")
public class SpringBootSopraApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SpringBootSopraApplication.class);

	private final Environment env;

	public SpringBootSopraApplication(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(SpringBootSopraApplication.class);
		Environment env = app.run(args).getEnvironment();
		String protocol = "http";

		LOG.info(
				"\n----------------------------------------------------------\n\t"
						+ "SpringBootSopraApplication '{}' is running! Access URLs:\n\t"
						+ "Local: \t\t{}://localhost:{}\n\t" + "External: \t{}://{}:{}\n\t"
						+ "Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"), protocol, env.getProperty("server.port"), protocol,
				InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), env.getActiveProfiles());
	}
}
