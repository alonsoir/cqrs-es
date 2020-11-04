package sopra.prototype.soprakafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringBootProducerApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootProducerApplication.class);
    private static final String topic = "topic1";

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProducerApplication.class, args).close();
    }

    @Autowired
    private KafkaTemplate<String, String> template;

    private final CountDownLatch latch = new CountDownLatch(3);

    @Override
    public void run(String... args) throws Exception {
        this.template.send(topic, "foo1");
        this.template.send(topic, "foo2");
        this.template.send(topic, "foo3");
        latch.await(60, TimeUnit.SECONDS);
        LOGGER.info("All received");
    }

    @KafkaListener(topics = "topic1")
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        LOGGER.info(cr.toString());
        latch.countDown();
    }

}