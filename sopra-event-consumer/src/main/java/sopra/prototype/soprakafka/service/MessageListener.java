package sopra.prototype.soprakafka.service;

import es.sopra.prototype.patterns.soprapatterns.observable.ServiceQueryObservable;
import es.sopra.prototype.patterns.soprapatterns.status.QueryStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import sopra.prototype.soprakafka.model.QueryMessage;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class MessageListener extends ServiceQueryObservable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final CountDownLatch latch = new CountDownLatch(1);
    private QueryStatus currentStatus;

    public MessageListener(){
        currentStatus = QueryStatus.Invoked;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    // ojito con ese topic hardcodeado!
    @KafkaListener(topics = "greetings-out")
    public void receive(String payload) {
        // Igual que antes, debes capturar la excepcion con un Try.of y actualizar el estado adecuadamente.
        LOGGER.info("received payload='{}'", payload);
        currentStatus = QueryStatus.ConsumedFromTopic;
        latch.countDown();
        // no estoy seguro si la última línea debe ser ésta o la de latch. TEST!
        notifyObservers(currentStatus);
    }

    // TODO ojito que esa definicion de topic debe estar en el yaml
    @KafkaListener(topics = "users-event-topic-out")
    public void receiveQueryMessage(final QueryMessage queryMessage){
        // Igual que antes, debes capturar la excepcion con un Try.of y actualizar el estado adecuadamente.
        LOGGER.info("received queryMessage='{}'", queryMessage);
        currentStatus = QueryStatus.ConsumedFromTopic;
        latch.countDown();
        // no estoy seguro si la última línea debe ser ésta o la de latch. TEST!
        notifyObservers(currentStatus);
    }
}
