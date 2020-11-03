package sopra.prototype.soprakafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;
import sopra.prototype.soprakafka.model.QueryMessage;
import sopra.prototype.soprakafka.service.MessageListener;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableKafka
public class KafkaConfiguration {

    //@Value("${spring.cloud.stream.kafka.binder.brokers}")
    private String bootstrapServers = "0.0.0.0:9092";

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // allows a pool of processes to divide the work of consuming and processing records
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "input-group-1");
        // automatically reset the offset to the earliest offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    @Bean
    public ConsumerFactory<String, QueryMessage> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, QueryMessage>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, QueryMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

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
}
