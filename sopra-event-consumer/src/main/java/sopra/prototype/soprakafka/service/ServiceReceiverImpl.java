package sopra.prototype.soprakafka.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sopra.prototype.soprakafka.model.ServiceReceiver;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ServiceReceiverImpl implements ServiceReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceReceiverImpl.class);

    @Autowired
    MessageListener messageListener;

    @Override
    public boolean getMessageFromTopic() throws InterruptedException {
        boolean countDownLatch = false;
            // I am not sure if this method is ever invoked... This is NOT cool...
            LOGGER.info("ServiceReceiverImpl.getMessageFromTopic...");
            countDownLatch = messageListener.getLatch().await(10, TimeUnit.SECONDS);
            LOGGER.info("getMessageFromTopic returned countDownLatch: " + countDownLatch);
        return countDownLatch;
    }
}
