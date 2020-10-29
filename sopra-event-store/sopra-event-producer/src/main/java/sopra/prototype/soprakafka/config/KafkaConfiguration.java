package sopra.prototype.soprakafka.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Component;
import sopra.prototype.soprakafka.stream.GreetingsStreams;

@Component
@EnableBinding(GreetingsStreams.class)
public class KafkaConfiguration {

    //resuming the consumer
    @Bean
    public ApplicationListener<ListenerContainerIdleEvent> idleListener() {
        return event -> {
            System.out.println("Trying to resume consumer: " + event);
            if (event.getConsumer().paused().size() > 0) {
                event.getConsumer().resume(event.getConsumer().paused());
            }
        };
    }
}
