package sopra.prototype.soprakafka.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.soprakafka.model.CommandMessage;

@Slf4j
public class MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    private final KafkaTemplate<String, CommandMessage> kafkaCommandsTemplate;

    @Autowired
    public MessageProducer(final KafkaTemplate<String, CommandMessage> kafkaCommandsTemplate) {
        LOGGER.info("Building MessageProducer instance from sopra-event-producer...");
        this.kafkaCommandsTemplate=kafkaCommandsTemplate;
    }

    public ListenableFuture<SendResult<String, CommandMessage>> sendMessageToTopic (Message<CommandMessage> message) {
        // TODO cambiar estos try catch por Try.of
        ListenableFuture<SendResult<String, CommandMessage>>  listenable =null;
        try {
            LOGGER.info("Sending message {}",message.toString());
            listenable = kafkaCommandsTemplate.send(message);
            LOGGER.info("listenable: " + listenable.toString());
        }catch (Exception e) {
            LOGGER.error("Something went wrong when sending entity to topic: " + e.getMessage());
        }catch (Throwable th) {
            LOGGER.error("Something went wrong when sending entity to topic: " + th.getMessage());
        }
        return listenable ;
    }
}
