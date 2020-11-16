package sopra.prototype.soprakafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.patterns.soprapatterns.observable.ServiceCommandObservable;
import sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import sopra.prototype.soprakafka.model.CommandMessage;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Service
@Slf4j
public class CommandServiceEventStoreImpl extends ServiceCommandObservable implements CommandServiceEventStore {

    @Autowired
    private MessageProducer producer;

    private CommandStatus currentStatus;

    @Value(value = "${command.topic.name}")
    private String topic;

    public CommandServiceEventStoreImpl() {

        // TODO tienes que indicar quien es el observador! si no, al notificar, la lista estará vacía y el observer
        //  no será notificado. Esto es un bug que ya sospechaba. En una implementación más seria, si quiero tener un
        // sistema distribuido, en contenedores, necesito un mecanismo de paso de mensaje real más que indicar desde
        // una variable de memoria compartida quien es el observador y quienes los observados. Me quedo con la idea.

        // addObserver();

    }

    public boolean sendCommandMessage(final CommandMessage message){
        // TODO Necesitas meter esta lógica en un Try.of para en caso de que haya alguna excepcion
        Message<CommandMessage> messageContained = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        boolean status=false;
        currentStatus = CommandStatus.Initialized;
        notifyObservers(currentStatus);
        log.info("Sendind Command message {}",message.toString());
        try{
            ListenableFuture<SendResult<String, CommandMessage>> listenable=  producer.sendMessageToTopic(messageContained);
            // I cannot use this isDone method because is not reliable to know if the message was sent.
            currentStatus =  CommandStatus.SavedEventStore;
            status=true;
        }catch(Exception e ){
            log.info("Something went wrong when sendind Command message {}", message.toString());
            log.error(e.getMessage());
            currentStatus =  CommandStatus.SavedFailedEventStore;
        }
        log.info("Sendind Command message status {}",currentStatus);
        notifyObservers(currentStatus) ;
        return status;
    }

}
