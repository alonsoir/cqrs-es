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
import sopra.prototype.services.config.QueryConfig;
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
 * Este test está comprobando la funcionalidad completa del ciclo CQRS/ES.
 *
 * Tiene que haber un serviceCommand que hace la insercion en la base de datos de escritura, despues se inserta el
 * evento
 * en un topic a través de messageProducer, es decir, si inserta la agregación para que haya un listener de kafka que
 * escuche a ese topic, lo consuma, y cree la proyeccion en la base de datos de lecturas.
 *
 *
 *
 *
 */
@SpringBootTest(classes={CommandConfig.class, QueryConfig.class,Config.class},
                properties = "spring.main.allow-bean-definition-overriding=true")
@Slf4j
public class ServiceFAKECommandHandlerTest {

    @Autowired
    private ServiceCommand serviceCommand;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private Listener listener;

    @Value(value = "${command.topic.name}")
    private String topic;

    @Test
    public void when_I_create_a_save_command_should_be_saved_in_commandBD_pushed_to_command_topic_pulled_from_that_topic_and_saved_in_QueryBD() throws InterruptedException {

        String actualDate = SopraUtils.getActualFormatedDate();
        // GIVEN a message to be stored in command cluster
        UserData user = new UserData();
        String name = "Papa-" + SopraUtils.getRandomUUID();
        user.setName(name);
        user.setDateRegister(actualDate);

        // WHEN Saving to Command Database Cluster
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);

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
        // WHEN pushing to kafka cluster creating the aggretation
        ListenableFuture<SendResult<String, CommandMessage>> messageSent = messageProducer
                                                                            .sendMessageToTopic(messageContained);
        assertNotNull(messageSent,"messageSent Should not be null");
        // THEN i can consume the message from topic and create the projection in query database cluster
        boolean isReceived  =listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isReceived,"isReceived should be true");
    }

    @Test
    public void when_I_create_a_delete_command_should_be_deleted_from_commandBD_pushed_to_command_topic_pulled_from_that_topic_and_deleted_from_QueryBD() throws InterruptedException {

        String actualDate = SopraUtils.getActualFormatedDate();

        // GIVEN a message to be stored in command cluster
        UserData user = new UserData();
        String name = "Mama"+ SopraUtils.getRandomUUID();
        user.setName(name);
        user.setDateRegister(actualDate);

        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);
        assertTrue(saved,"saved should be true");

        // THEN
        List<UserData>  listUsers = serviceCommand.findByName(name);
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"listUsers size should be greater than zero");
        assertTrue(listUsers.size()==1,"listUsers size should be only one");
        UserData userRecovered = listUsers.get(0);
        assertNotNull(userRecovered,"userRecovered should not be null");

        // WHEN updating the Command Database Cluster
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

        // TODO Esto no puede ser así, si he borrado algo en el command cluster, en el query cluster debería pasar lo
        // mismo! Podría detectar en el payload la palabra DELETE para saber que tengo que hacer un borrado.
        // Se puede hacer muchas cosas, desde meter la accion a realizar en la cabecera, como parte del mensaje del
        // payload, un campo nuevo.
        ListenableFuture<SendResult<String, CommandMessage>> messageSent = messageProducer
                .sendMessageToTopic(messageContained);
        assertNotNull(messageSent,"Should not be null");
        // THEN i can consume the message from topic and create the projection in query database cluster
        boolean isReceived  =listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isReceived,"isReceived should be true");
    }
}
