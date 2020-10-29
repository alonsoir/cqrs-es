package sopra.prototype.soprakafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import sopra.prototype.soprakafka.model.Greetings;
import sopra.prototype.soprakafka.service.GreetingsService;

import javax.annotation.PostConstruct;
import java.util.Random;

@SpringBootApplication
@EnableBinding(Sink.class)
public class SopraKafkaApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SopraKafkaApplication.class);
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	@Autowired
	private final GreetingsService greetingsService;

	// random data to send...
	private static final String[] data = new String[] {
			"foo1", "bar1", "qux1",
			"foo2", "bar2", "qux2",
			"foo3", "bar3", "qux3",
			"foo4", "bar4", "qux4",
	};

	public SopraKafkaApplication(GreetingsService greetingsService) {
		this.greetingsService = greetingsService;
	}


	public static void main(String[] args) {
		new SpringApplicationBuilder(SopraKafkaApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);

		//SpringApplication.run(SopraKafkaApplication.class, args).web();
	}

	/*
	@PostConstruct
	public void sendingSomeMessages(){

		String simpleMessage = "simpleMessage";
		greetingsService.sendSimpleMessage(simpleMessage);

		Greetings greetings = Greetings.builder().message(simpleMessage).timestamp(System.currentTimeMillis()).build(); ;
		greetingsService.sendGreeting(greetings);
	}
	*/

/*
	@StreamListener(Sink.INPUT)
	public void process(Message<?> message) {
		Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
		if (acknowledgment != null) {
			LOGGER.info("Acknowledgment provided");
			acknowledgment.acknowledge();
		}
	}

	// Example: Pausing the consumer
	@StreamListener(Sink.INPUT)
	public void in(String in, @Header(KafkaHeaders.CONSUMER) Consumer<?, ?> consumer) {
		LOGGER.info("----> pausing: " + in);
		consumer.pause(Collections.singleton(new TopicPartition("event-store-topic", 0)));
	}

	// Apache Kafka supports topic partitioning natively.
	@InboundChannelAdapter(channel = Source.OUTPUT, poller = @Poller(fixedRate = "5000"))
	public Message<?> generate() {
		String value = data[RANDOM.nextInt(data.length)];
		LOGGER.info("----> Sending random data: " + value);
		return MessageBuilder.withPayload(value)
				.setHeader("partitionKey", value)
				.build();
	}
*/
	//listens to a Kafka stream and prints (to the console) the partition ID to which each message goes:
	@StreamListener(Sink.INPUT)
	public void listen(@Payload String in, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		System.out.println(in + " received from partition " + partition);
	}
}
