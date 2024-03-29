package sopra.prototype.soprakafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.services.config.QueryConfig;
import sopra.prototype.soprakafka.config.Config;
import sopra.prototype.soprakafka.listeners.Listener;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.MessageProducer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/***
 * Este test sirve para demostrar la funcionalidad básica de cada uno de los componentes de la arquitectura.
 */
@SpringBootTest(classes={QueryConfig.class,Config.class})
public class QuickTests {

    private static final Logger logger = LoggerFactory.getLogger(QuickTests.class);
    // usados para el contenedor autogestionado por esta prueba unitario.
    private final String topic1 = "topic1";
    private final Object group = "group1";

    @Value(value = "${command.topic.name}")
    private String topic;

    @Autowired
    private Listener listener;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Actualmenente solo consigo poder instanciar MessageProducer y Listener para que puedan ser usados fuera de
    // este contexto. No puedo usar CommandServiceEventStore, ni probablemente QueryServiceEventStore. Por ahora.
    // Creo que si este proyecto estuviera compartiendo lugar con ServiceCommand y ServiceQuery, no habría problema.
    @Autowired
    private MessageProducer messageProducer;

    // Usar esto introduce una dependencia circular. DESACTIVANDO
    //@Autowired
    //private CommandServiceEventStore commandServiceEventStore;
/*
    @Test
    public void testPushingCommandMessageAndReceivedUsing_kafkaListenerCommandContainerFactory() throws InterruptedException {
        logger.info("testPushingCommandMessageAndReceivedUsing_kafkaListenerCommandContainerFactory");
        CommandMessage message = CommandMessage.builder()
                .message("This is another message using commandService-" + randomUUID())
                .dateRegister(getActualFormatedDate())
                .timestamp(System.currentTimeMillis())
                .name(randomUUID())
                .build();
        boolean isSent = commandServiceEventStore.sendCommandMessage(message);
        assertTrue(isSent,"isSent should be true if message was sent.");

        boolean isReceived = this.listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isReceived,"isReceived should be false because it will be true if the countdown is reached...");
    }

    @Test
    public void testPushingCommandUsingCommandServiceEventStore() throws InterruptedException{

        logger.info("testPushingCommandMessageUsingMessageProducer");
        CommandMessage message = CommandMessage.builder()
                .message("This is another message using commandService-" + randomUUID())
                .dateRegister(getActualFormatedDate())
                .timestamp(System.currentTimeMillis())
                .name(randomUUID())
                .build();
        boolean isSent = commandServiceEventStore.sendCommandMessage(message);
        assertTrue(isSent,"isSent should be true if message was sent.");

        boolean isReceived = this.listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isReceived,"isReceived should be false because it will be true if the countdown is reached...");
    }
*/
    @Test
    public void testPushingCommandMessageUsingMessageProducer_ConsumingFromTopic_Saving_TO_QueryCluster() throws InterruptedException {

        logger.info("testPushingCommandMessageUsingMessageProducer_ConsumingFromTopic_Saving_TO_QueryCluster");
        // GIVEN
        CommandMessage message = CommandMessage.builder()
                                               .message("This is a message" + randomUUID())
                                               .dateRegister(getActualFormatedDate())
                                               .timestamp(System.currentTimeMillis())
                                               .name("RANDOM_NAME-" +randomUUID())
                                               .build();
        Message<CommandMessage> messageContained = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        // WHEN
        ListenableFuture<SendResult<String, CommandMessage>> messageSent = messageProducer.sendMessageToTopic(messageContained);
        assertNotNull(messageSent);
        boolean isSentAndSavedIntoQueryCluster = this.listener.getLatch1().await(1, TimeUnit.SECONDS);
        // THEN
        assertTrue(isSentAndSavedIntoQueryCluster,"should be true because it will be true if the countdown is reached...");
    }

    @Test
    public void testSimple() throws InterruptedException {
        logger.info("testSimple");
        kafkaTemplate.send(topic1, "key0", "Alonso is testing using a Junit5 test file...");
        kafkaTemplate.flush();
        boolean isSent = this.listener.getLatch1().await(1, TimeUnit.SECONDS);
        assertTrue(isSent,"should be false because it will be true if the countdown is reached...");
    }



    @Test
    public void testAutoCommit() throws InterruptedException {
        logger.info("Start auto");
        ContainerProperties containerProps = new ContainerProperties("topic1", "topic2");
        final CountDownLatch latch = new CountDownLatch(4);

        containerProps.setMessageListener(new MessageListener<Integer, String>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                logger.info("received: " + message);
                latch.countDown();
            }

        });
        KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
        container.setBeanName("testAuto");
        container.start();
        Thread.sleep(1000); // wait a bit for the container to start
        KafkaTemplate<String, String> template = createTemplate();
        template.setDefaultTopic(topic1);
        template.sendDefault("0", "foo");
        template.sendDefault("2", "bar");
        template.sendDefault("0", "baz");
        template.sendDefault("2", "qux");
        template.flush();
        boolean isDataSent = latch.await(10, TimeUnit.SECONDS);
        assertTrue(isDataSent,"should be false because it will be true if the countdown is reached...");
        container.stop();
        logger.info("Stop auto");

    }

    private KafkaMessageListenerContainer<Integer, String> createContainer(ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<Integer, String>(props);
        KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(cf, containerProps);
        return container;
    }

    private KafkaTemplate<String, String> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<String, String>(senderProps);
        return new KafkaTemplate<String, String>(pf);
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
    private static String getActualFormatedDate() {
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }
    private String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
