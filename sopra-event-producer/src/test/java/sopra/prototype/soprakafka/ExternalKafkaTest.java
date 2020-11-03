package sopra.prototype.soprakafka;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.CommandServiceEventStore;
import sopra.prototype.soprakafka.service.MessageProducer;
import sopra.prototype.soprakafka.stream.CommandStreams;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExternalKafkaTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalKafkaTest.class);

    private static final String TOPIC1 = CommandStreams.OUTPUT;
    private static final String BROKER = "0.0.0.0:9092";

    @Autowired
    private CommandServiceEventStore commandServiceEventStore;

    public ExternalKafkaTest (){
    }

    @BeforeClass
    public static void setup() {

        System.setProperty("spring.cloud.stream.kafka.binder.brokers",BROKER );
        System.setProperty("spring.cloud.stream.bindings.input.destination", TOPIC1);
        System.setProperty("spring.cloud.stream.bindings.input.content-type", "text/plain");
        System.setProperty("spring.cloud.stream.bindings.input.group", "input-group-1");
        System.setProperty("spring.cloud.stream.bindings.output.destination", TOPIC1);
        System.setProperty("spring.cloud.stream.bindings.output.content-type", "text/plain");
        System.setProperty("spring.cloud.stream.bindings.output.group", "output-group-1");

    }

    @Test
    public void testSendCommandMessaged() {

        LOGGER.info("testMessageSendRecieve_Awaitility");
        // GIVEN
        String message = "testMessageSendRecieve_Awaitility";
        CommandMessage messageCommand = CommandMessage.builder()
                                                .message(message)
                                                .timestamp(System.currentTimeMillis())
                                                .dateRegister(getActualFormatedDate()).build();
        // WHEN
        //ListenableFuture<SendResult<String, CommandMessage>> listener = producer.sendMessageToTopic(messageCommand);
        //boolean sent = listener.isDone();
        boolean sent = commandServiceEventStore.sendCommandMessage(messageCommand);
        assertTrue("Should be TRUE if sent.",sent);

    }

    // TODO refactorizar este método pq está duplicado en ServiceCommandHandlerImpl.
    // Hay que meterlo en algo como sopra-utils.
    private static String getActualFormatedDate() {
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }
}
