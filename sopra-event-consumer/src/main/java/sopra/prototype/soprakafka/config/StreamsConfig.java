package sopra.prototype.soprakafka.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import sopra.prototype.soprakafka.stream.GreetingsStreams;

@EnableBinding(GreetingsStreams.class)
public class StreamsConfig {
}
