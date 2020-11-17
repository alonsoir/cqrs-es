package sopra.prototype.soprakafka.listeners;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import sopra.prototype.services.bd.ServiceQuery;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.vo.UserData;

import java.util.concurrent.CountDownLatch;

/***
 * Necesito que esta clase provea la manera de interactuar con la base de lecturas al recibir el CommandMessage.
 * Tiene que ser así porque no hay otra manera, que yo conozca.
 */
@Slf4j
public class Listener {

    @Autowired
    private ServiceQuery serviceQuery;

    private final CountDownLatch latch1 = new CountDownLatch(1);

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);



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
        UserData user = new UserData();
        user.setName(commandMessage.getName());
        user.setDateRegister(commandMessage.getDateRegister());
        boolean isUsersSavedIntoQueryCluster = serviceQuery.saveOrUpdateIntoDB(user);
        logger.info("---> .isUsersSavedIntoQueryCluster? {}",isUsersSavedIntoQueryCluster);

    }
// Should be private but, if i want to use it in tests...
    public CountDownLatch getLatch1() {
        logger.info("---> Listener.getLatch1 method. ");

        return latch1;
    }


}
