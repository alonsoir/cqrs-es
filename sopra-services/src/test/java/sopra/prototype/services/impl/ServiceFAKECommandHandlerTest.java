package sopra.prototype.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.command.services.ServiceCommand;
import sopra.prototype.config.services.CommandConfig;
import sopra.prototype.services.impl.utils.SopraUtils;
import sopra.prototype.soprakafka.config.Config;
import sopra.prototype.soprakafka.listeners.Listener;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.MessageProducer;
import sopra.prototype.vo.UserData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/***
 * Este test trata de simular el comportamiento de la clase ServiceCommandHandlerImpl. Debe poder guardar en base de
 * datos y lanzar al topic kafka el evento agregado.
 * No soy capaz de que Spring instancie CommandServiceEventStoreImpl, solo consigo que instancie MessageProducer que
 * está siendo instanciado como un @Bean en la clase sopra.prototype.soprakafka.config.Config.
 * Lo puedo instanciar al menos...
 *
 * Puedo recuperar también los mensajes a través del Listener instanciado en sopra.prototype.soprakafka.config.Config
 *
 * Por qué no puedo instanciar ServiceCommandHandlerImpl???? voy a tener que usar estas funcionalidades en el controller
 * sin poder usar el Handler?
 */
@SpringBootTest(classes={CommandConfig.class,Config.class})
@Slf4j
public class ServiceFAKECommandHandlerTest {

    @Autowired
    private ServiceCommand serviceCommand;

    //@Autowired
    //private CommandServiceEventStore commandServiceEventStore;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private Listener listener;

    @Value(value = "${command.topic.name}")
    private String topic;

    @Test
    public void testSaveOrUpdateIntoDB() throws InterruptedException {

        String actualDate = SopraUtils.getActualFormatedDate();
        // GIVEN
        UserData user = new UserData();
        String name = "Papa";
        user.setName(name);
        user.setDateRegister(actualDate);

        // WHEN
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);

        // THEN
        assertTrue(saved,"saved should be true");
        List<UserData> listUsers = serviceCommand.listAll();
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"Should be greater than zero.");

        CommandMessage payload = CommandMessage.builder()
                                                      .message("Some dummy message to be agregated into Kafka ES " +
                                                              "after a SAVING action.")
                                                      .dateRegister(actualDate)
                                                      .name(name)
                                                      .timestamp(System.currentTimeMillis())
                                                      .build();

        Message<CommandMessage> messageContained = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        ListenableFuture<SendResult<String, CommandMessage>> messageSent = messageProducer
                                                                            .sendMessageToTopic(messageContained);
        assertNotNull(messageSent,"Should not be null");

        boolean isReceived = this.listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isReceived,"isReceived should be true because the countdown is reached...");
    }

    @Test
    public void testdeleteFromDB() throws InterruptedException {

        String actualDate = SopraUtils.getActualFormatedDate();

        // GIVEN
        UserData user = new UserData();
        String name = "Mama";
        user.setName(name);
        user.setDateRegister(actualDate);

        // WHEN
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);
        assertTrue(saved,"saved should be true");

        // THEN
        List<UserData>  listUsers = serviceCommand.findByName(name);
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"listUsers size should be greater than zero");
        assertTrue(listUsers.size()==1,"listUsers size should be only one");
        UserData userRecovered = listUsers.get(0);
        assertNotNull(userRecovered,"userRecovered should not be null");
        boolean isDeleted = serviceCommand.deleteFromDB(userRecovered.getIdUserData());
        assertTrue(isDeleted,"isDeleted should be true");

        CommandMessage payload = CommandMessage.builder()
                .message("Some dummy message to be agregated into Kafka ES " +
                        "after a DELETE action.")
                .dateRegister(actualDate)
                .name(name)
                .timestamp(System.currentTimeMillis())
                .build();

        Message<CommandMessage> messageContained = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        ListenableFuture<SendResult<String, CommandMessage>> messageSent = messageProducer
                .sendMessageToTopic(messageContained);
        assertNotNull(messageSent,"Should not be null");

        boolean isReceived = this.listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isReceived,"isReceived should be true because the countdown is reached...");
    }
}
