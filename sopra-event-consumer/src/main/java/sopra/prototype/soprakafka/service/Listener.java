package sopra.prototype.soprakafka.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import sopra.prototype.soprakafka.model.QueryMessage;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Listener  {
// todo este listener deberá tener inyectado el servicio que hace la insercion en la bd de lecturas...
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {

        return latch;
    }

    // TODO habrá que quitarlo y ojito con ese topic hardcodeado!
    /*
    @KafkaListener(topics = "greetings-out")
    public void receive(String payload) {
        // Igual que antes, debes capturar la excepcion con un Try.of y actualizar el estado adecuadamente.
        LOGGER.info("received payload='{}'", payload);
        latch.countDown();
        // no estoy seguro si la última línea debe ser ésta o la de latch. TEST!
    }
    */

    // TODO ojito que esa definicion de topic debe estar en el yaml
    @KafkaListener(topics = "users-event-topic-out")
    public void receiveQueryMessage(final QueryMessage queryMessage){
        // Igual que antes, debes capturar la excepcion con un Try.of y actualizar el estado adecuadamente.
        LOGGER.info("received queryMessage='{}'", queryMessage);
        latch.countDown();
    }
}
