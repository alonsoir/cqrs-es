package sopra.prototype.soprakafka.service;

import es.sopra.prototype.patterns.soprapatterns.observable.ServiceCommandObservable;
import es.sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.soprakafka.model.CommandMessage;

@Service
@Slf4j
public class CommandServiceEventStoreImpl extends ServiceCommandObservable implements CommandServiceEventStore {

    private final MessageProducer producer;

    private CommandStatus currentStatus;

    @Autowired
    public CommandServiceEventStoreImpl(final MessageProducer producer) {

        // para borrar en cuanto tenga el test
        this.producer = producer;
        //MessageChannel messageChannel = commandStreams.outboundGreetings();
        //Necesitaría algo que indicara que he inicializado el EventStore?
        currentStatus = CommandStatus.Initialized;
        notifyObservers(currentStatus);
    }

    public boolean sendCommandMessage(final CommandMessage message){
        // Necesitas meter esta lógica en un Try.of para en caso de que haya alguna excepcion
        // poner el estado adecuado.
        log.info("Sendind Command message {}",message);
        ListenableFuture<SendResult<String, CommandMessage>> listenable=  producer.sendMessageToTopic(message);
        boolean messageSent = listenable.isDone();
        CommandStatus status = messageSent ? currentStatus = CommandStatus.SavedEventStore :
                CommandStatus.SavedFailedEventStore;
        notifyObservers(status) ;
        return messageSent;
    }

}
