package sopra.prototype.soprakafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.StringUtils;
import sopra.prototype.soprakafka.listeners.Listener;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.MessageProducer;

import java.util.HashMap;
import java.util.Map;

/***
 * Configuracion tanto para el consumidor como el productor que usa Kafka.
 * Está leyendo del fichero application.properties que está en sopra-spring.
 * Tenlo en cuenta para cuando tengas que refactorizar o cambiar algo.
 *
 */
@Configuration
@EnableKafka
public class Config {

    // todo esto tiene que pillar valor del application.properties de sopra-spring.
    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String broker;

    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String  brokers[];

    @Value(value = "${command.topic.name}")
    private String topic;

    @Value(value = "${spring.kafka.producer.group-id}")
    private String groupId;

    // ojito, que esto lo está leyendo de application-query.properties
    @Value(value = "${spring.kafka.consumer.max.poll.interval.ms}")
    private String maxConsumerInterval;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                StringUtils.arrayToCommaDelimitedString(brokers));
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        // topicName, numPartitions, replicationFactor.
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

    @Bean
    public Listener listener() {
        return new Listener();
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CommandMessage> kafkaListenerCommandContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommandMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerCommandFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CommandMessage> consumerCommandFactory() {
        JsonDeserializer<CommandMessage> deserializer = new JsonDeserializer<>(CommandMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,maxConsumerInterval);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }


    @Bean
    public KafkaListenerContainerFactory<?> kafkaJsonListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommandMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerCommandFactory());
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public Map<String, Object> producerCommandsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return props;
    }

    @Bean
    public ProducerFactory<String, CommandMessage> producerCommandFactory() {
        return new DefaultKafkaProducerFactory<>(producerCommandsConfigs());
    }

    @Bean
    public KafkaTemplate<String, CommandMessage> kafkaCommandsTemplate(){
        return new KafkaTemplate<String,CommandMessage>(producerCommandFactory());
    }

    @Bean
    public MessageProducer messageProducer(){
        return new MessageProducer(kafkaCommandsTemplate());
    }
}