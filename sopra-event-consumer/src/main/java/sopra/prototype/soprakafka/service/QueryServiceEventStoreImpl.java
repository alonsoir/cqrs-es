package sopra.prototype.soprakafka.service;

import sopra.prototype.vo.UserData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sopra.prototype.patterns.soprapatterns.observable.ServiceQueryObservable;
import sopra.prototype.patterns.soprapatterns.status.QueryStatus;
import sopra.prototype.soprakafka.model.QueryMessage;
import sopra.prototype.services.bd.ServiceQuery;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QueryServiceEventStoreImpl extends ServiceQueryObservable implements QueryServiceEventStore{

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryServiceEventStoreImpl.class);

    private Listener listener;
    private ServiceQuery serviceQuery;

    private QueryStatus currentStatus;

    @Autowired
    public QueryServiceEventStoreImpl(Listener listener,ServiceQuery serviceQuery){
        this.serviceQuery = serviceQuery;
        this.listener = listener;
        currentStatus = QueryStatus.Invoked;
        notifyObservers(currentStatus);
    }
    @Override
    @KafkaListener(topics = "users-event-topic-out")
    public void consume(QueryMessage queryMessage) {
        LOGGER.info("Consumind queryMessage {}",queryMessage.toString());
        try {
            boolean isReceived = this.listener.getLatch().await(1, TimeUnit.SECONDS);
            LOGGER.info("isReceived? {}",isReceived);
            currentStatus = QueryStatus.ConsumedFromTopic;
            // ahora puedo interactuar con la base de datos de lecturas, puede que tenga que transformar el mensaje...
            // tengo que inyectar aqui la dependencia para interactuar con el servicio para hacer la escritura en la
            // bd de lecturas.
            UserData user = new UserData();
            user.setName(queryMessage.getName());
            user.setDateRegister(queryMessage.getDateRegister());
            serviceQuery.saveOrUpdateIntoDB(user);
        }catch (InterruptedException e){
            LOGGER.error("InterruptedException al recibir el mensaje");
            currentStatus = QueryStatus.ErrorConsumingFromTopic;
        }
        notifyObservers(currentStatus);

    }
}
