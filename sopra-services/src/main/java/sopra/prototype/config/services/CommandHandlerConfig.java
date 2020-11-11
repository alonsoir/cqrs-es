package sopra.prototype.config.services;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("sopra.prototype.soprakafka.model")
@ComponentScan("sopra.prototype.patterns.soprapatterns")
@ComponentScan("sopra.prototype.soprakafka.service")
@ComponentScan("sopra.prototype.command.services")
public class CommandHandlerConfig {

}
