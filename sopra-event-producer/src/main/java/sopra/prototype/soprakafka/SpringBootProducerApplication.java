package sopra.prototype.soprakafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(scanBasePackages = "sopra.prototype")
@EntityScan(basePackages = "sopra.prototype")
public class SpringBootProducerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootProducerApplication.class);

    private final Environment env;

    public SpringBootProducerApplication( Environment env){
      this.env = env;
    }

    public static void main(String args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(SpringBootProducerApplication.class);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";

        LOG.info(
                "\n----------------------------------------------------------\n\t"
                        + "SpringBootProducerApplication '{}' is running! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}\n\t" + "External: \t{}://{}:{}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), protocol, env.getProperty("server.port"), protocol,
                InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), env.getActiveProfiles());
    }
}
