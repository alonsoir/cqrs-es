package sopra.prototype.command.handler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFuture;
import sopra.prototype.command.services.ServiceCommand;
import sopra.prototype.patterns.soprapatterns.observer.ServiceCommandObserver;
import sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import sopra.prototype.soprakafka.service.MessageProducer;
import sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.CommandServiceEventStore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
//@ComponentScan("sopra.prototype.soprakafka.service")
//@ComponentScan("sopra.prototype.command.services")
public class ServiceCommandHandlerImpl implements ServiceCommandHandler, ServiceCommandObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandHandlerImpl.class);

    @Autowired
    private ServiceCommand serviceCommand;
    @Autowired
    private MessageProducer messageProducer;

    @Value(value = "${command.topic.name}")
    private String topic;

    public ServiceCommandHandlerImpl() {

        LOGGER.info("---> BuildingServiceCommandHandlerImpl.");
        //this.serviceCommand = serviceCommand;
        //this.eventStore = eventStore;
    }


    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        LOGGER.info("ServiceCommandHandlerImpl.saveOrUpdateIntoDB. user: " + user.toString());
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);
        // ojito con esto, la agregacion que pushearas al topic se parecerá al entity guardado, pero no será igual.
        // habrá que hacer una conversion, o creacion, ya veremos
        CommandMessage message = CommandMessage.builder()
                .timestamp(System.currentTimeMillis())
                .message("User " + user.getName() + " was created at " + LocalDate.now())
                .dateRegister(user.getDateRegister())
                .name(user.getName())
                .build();


        boolean agregationCreationPushedToEventStore= pushIntoEventStore(message);
        return agregationCreationPushedToEventStore;
    }

    @Override
    public boolean deleteFromDB(Integer id) {
        LOGGER.info("ServiceCommandHandlerImpl.deleteFromDB. id: " + id);
        boolean deletedFromDB = serviceCommand.deleteFromDB(id);
        // El cambio de estado, cuando lo hago, aquí?
        // No, ocurre en la implementacion de ServiceCommand
        String formattedString = getActualFormatedDate();

        CommandMessage message = CommandMessage.builder()
                .timestamp(System.currentTimeMillis())
                .message("User " + id + " was deleted at " + LocalDate.now())
                .dateRegister(formattedString)
                .name(String.valueOf(id))
                .build();

        boolean pushedToTopic = pushIntoEventStore(message);
        LOGGER.info("deletedFromDB : " + deletedFromDB + " pushedToTopic: " + pushedToTopic);
        return deletedFromDB & pushedToTopic;
    }

    @Override
    public List<UserData> listAll() {
        LOGGER.info("ServiceCommandHandlerImpl.listAll.");
        return serviceCommand.listAll();
    }

    @Override
    public List<UserData> findByName(String name) {
        LOGGER.info("ServiceCommandHandlerImpl.findByName. {}",name);
        return serviceCommand.findByName(name);
    }

    // todo esta funcion debe estar en algun proyecto con funciones comunes, pq ahora mismo está en tres sitios.
    //  INACEPTABLE
    private static String getActualFormatedDate() {
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }

    public  boolean pushIntoEventStore(CommandMessage command) {
        LOGGER.info("ServiceCommandHandlerImpl.pushIntoEventStore, {}",command.toString());
        Message<CommandMessage> messageContained = MessageBuilder
                .withPayload(command)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        ListenableFuture<SendResult<String, CommandMessage>> pushed = messageProducer.sendMessageToTopic(messageContained);
        LOGGER.info("ServiceCommandHandlerImpl.pushIntoEventStore, {} {}",command.toString(),pushed.toString());
        return pushed.isDone();
    }

    public void updateCommandStatus(CommandStatus status) {
        // Este método comprueba la actualizacion de estado. Podríamos guardar el estado, etc...
        LOGGER.info("ServiceCommandHandlerImpl updated status: " + status.getDescription());
    }
}
