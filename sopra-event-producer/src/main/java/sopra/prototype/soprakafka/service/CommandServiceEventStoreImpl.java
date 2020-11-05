package sopra.prototype.soprakafka.service;

import sopra.prototype.patterns.soprapatterns.observable.ServiceCommandObservable;
import sopra.prototype.patterns.soprapatterns.status.CommandStatus;
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

        this.producer = producer;
        currentStatus = CommandStatus.Initialized;
        notifyObservers(currentStatus);
    }

    public boolean sendCommandMessage(final CommandMessage message){
        // TODO Necesitas meter esta lógica en un Try.of para en caso de que haya alguna excepcion
        // poner el estado adecuado. y devolver el boolean adecuado
        boolean status=false;
        CommandStatus commandStatus;
        log.info("Sendind Command message {}",message.toString());
        try{
            ListenableFuture<SendResult<String, CommandMessage>> listenable=  producer.sendMessageToTopic(message);
            // I cannot use this isDone method because is not reliable to know if the message was sent.
            commandStatus =  CommandStatus.SavedEventStore;
            status=true;
        }catch(Exception e ){
            log.info("Something went wrong when sendind Command message {}", message.toString());
            log.error(e.getMessage());
            commandStatus =  CommandStatus.SavedFailedEventStore;
        }
        log.info("Sendind Command message status {}",commandStatus);
        notifyObservers(commandStatus) ;
        return status;
    }

}
