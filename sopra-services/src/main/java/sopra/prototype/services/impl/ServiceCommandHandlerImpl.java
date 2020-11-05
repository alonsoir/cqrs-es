package sopra.prototype.services.impl;

import sopra.prototype.patterns.soprapatterns.observer.ServiceCommandObserver;
import sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import sopra.prototype.services.bd.ServiceCommand;
import sopra.prototype.services.handler.ServiceCommandHandler;
import sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.CommandServiceEventStore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ServiceCommandHandlerImpl implements ServiceCommandHandler, ServiceCommandObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandHandlerImpl.class);

    private final ServiceCommand serviceCommand;
    private final CommandServiceEventStore eventStore;

    public ServiceCommandHandlerImpl(ServiceCommand serviceCommand, CommandServiceEventStore eventStore) {

        this.serviceCommand = serviceCommand;
        this.eventStore = eventStore;
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
        boolean pushed = eventStore.sendCommandMessage(command);
        LOGGER.info("ServiceCommandHandlerImpl.pushIntoEventStore, {} {}",command.toString(),pushed);
        return pushed;
    }

    public void updateCommandStatus(CommandStatus status) {
        // Este método comprueba la actualizacion de estado. Podríamos guardar el estado, etc...
        LOGGER.info("ServiceCommandHandlerImpl updated status: " + status.getDescription());
    }
}