package es.sopra.prototype.services.impl;

import es.sopra.prototype.services.bd.ServiceCommand;
import es.sopra.prototype.services.handler.ServiceCommandHandler;
import es.sopra.prototype.services.observer.ServiceCommandObserver;
import es.sopra.prototype.services.status.CommandStatus;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.service.CommandServiceEventStore;

import java.time.LocalDate;

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
        LOGGER.info("ServiceCommandHandlerImpl.saveOrUpdateIntoDB");
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
    public boolean deleteFromDB(Long id) {
        LOGGER.info("ServiceCommandHandlerImpl.deleteFromDB");
        boolean deleted = serviceCommand.deleteFromDB(id);
        // TODO hay que hacer un mapeador o builder para crear el objeto agregador que muestra el borrado que
        //  pushearemos en el Event Store.
        //UserData agregationDeletePushedToEventStore = pushIntoEventStore(deleted);

        return deleted;
    }

    public  boolean pushIntoEventStore(CommandMessage command) {
        LOGGER.info("ServiceCommandHandlerImpl.pushIntoEventStore");
        return eventStore.sendCommandMessage(command);
    }

    public void updateCommandStatus(CommandStatus status) {
        // Este método comprueba la actualizacion de estado. Podríamos guardar el estado, etc...
        LOGGER.info("ServiceCommandHandlerImpl updated status: " + status.getDescription());
    }
}
