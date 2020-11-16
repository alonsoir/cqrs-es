package sopra.prototype.soprakafka.listeners;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import sopra.prototype.soprakafka.model.CommandMessage;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Listener {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    private final CountDownLatch latch1 = new CountDownLatch(1);

    @KafkaListener(id = "commands-strings", topics = "${command.topic.name}", containerFactory =
            "kafkaListenerContainerFactory")
    public void listen1(String foo) {
        logger.info("---> Listener.listen1 method. {}",foo);
        this.getLatch1().countDown();

    }

    @KafkaListener(id = "commands-messages",
                   topics = "${command.topic.name}",
                   groupId = "${spring.kafka.producer.group-id}",
                   containerFactory = "kafkaListenerCommandContainerFactory")
    public void listenToCommands(@Payload CommandMessage commandMessage,@Headers MessageHeaders headers) {

        // tengo acceso a la cabecera. es un mapa
        logger.info("---> Listener.listenToCommands method. {} {}",commandMessage.toString(),headers.toString());
        this.getLatch1().countDown();

    }

    public CountDownLatch getLatch1() {
        logger.info("---> Listener.getLatch1 method. ");

        return latch1;
    }
}
