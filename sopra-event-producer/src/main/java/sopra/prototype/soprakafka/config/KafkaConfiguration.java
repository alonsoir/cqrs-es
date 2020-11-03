package sopra.prototype.soprakafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.support.serializer.JsonSerializer;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.MessageProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${command.topic.name}")
    private String commandTopicName;

    @Value(value = "${kafka.numPartitions}")
    private int numPartitions;

    @Value(value = "${kafka.replicationFactor}")
    private short replicationFactor;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public ProducerFactory<String, CommandMessage> commandProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        configProps.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        //configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "getting-things-done");
        final DefaultKafkaProducerFactory<String, CommandMessage> factory =
                new DefaultKafkaProducerFactory<>(configProps);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, CommandMessage> kafkaTemplate(@Autowired ProducerFactory<String, CommandMessage> factory) {
        return new KafkaTemplate<>(factory);
    }

    /*
    @Bean
    public KafkaTemplate<String, CommandMessage> commandKafkaTemplate() {
        return new KafkaTemplate<>(commandProducerFactory());
    }

     */
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

    @Bean
    public MessageProducer messageProducer() {

        return new MessageProducer();
    }

}
