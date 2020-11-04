package sopra.prototype.soprakafka.listeners;

import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Listener {

    private final CountDownLatch latch1 = new CountDownLatch(1);

    @KafkaListener(id = "foo", topics = "test1")
    public void listen1(String foo) {
        this.getLatch1().countDown();
    }

    public CountDownLatch getLatch1() {
        return latch1;
    }
}
