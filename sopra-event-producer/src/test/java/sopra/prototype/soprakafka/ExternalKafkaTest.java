package sopra.prototype.soprakafka;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sopra.prototype.soprakafka.model.Greetings;
import sopra.prototype.soprakafka.service.GreetingsService;

@SpringBootTest
public class ExternalKafkaTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalKafkaTest.class);

    private static final String TOPIC1 = "greetings";
    private static final String BROKER = "0.0.0.0:9092";

    @Autowired
    private GreetingsService greetingsService;

    public ExternalKafkaTest(){

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
    public void testMessageSendRecieve_Awaitility() {

        LOGGER.info("testMessageSendRecieve_Awaitility");
        // GIVEN
        String message = "testMessageSendRecieve_Awaitility";
        Greetings greetings = Greetings.builder().message(message).timestamp(System.currentTimeMillis()).build();
        // WHEN
        greetingsService.sendGreeting(greetings);
        // THEN como puedo comprobar la recoleccion del mensaje?

    }
}
