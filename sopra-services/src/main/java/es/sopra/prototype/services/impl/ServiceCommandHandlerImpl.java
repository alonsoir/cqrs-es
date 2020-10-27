package es.sopra.prototype.services.impl;

import es.sopra.prototype.services.bd.ServiceCommand;
import es.sopra.prototype.services.eventstore.ServiceCommandEventStore;
import es.sopra.prototype.services.handler.ServiceCommandHandler;
import es.sopra.prototype.services.observer.ServiceCommandObserver;
import es.sopra.prototype.services.status.CommandStatus;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceCommandHandlerImpl implements ServiceCommandHandler, ServiceCommandObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandHandlerImpl.class);

    private final ServiceCommand serviceCommand;
    private final ServiceCommandEventStore serviceCommandEventStore;

    @Autowired
    public ServiceCommandHandlerImpl(ServiceCommand serviceCommand,ServiceCommandEventStore serviceCommandEventStore) {
        this.serviceCommand = serviceCommand;
        this.serviceCommandEventStore = serviceCommandEventStore;
    }

    @Override
    public UserData saveOrUpdateIntoDB(UserData user) {
        LOGGER.info("ServiceCommandHandlerImpl.saveOrUpdateIntoDB");
        UserData saved = serviceCommand.saveOrUpdateIntoDB(user);
        // ojito con esto, la agregacion que pushearas al topic se parecerá al entity guardado, pero no será igual.
        // habrá que hacer una conversion, o creacion, ya veremos
        UserData agregationCreationPushedToEventStore = pushIntoEventStore(saved);
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

    @Override
    public UserData pushIntoEventStore(UserData data) {
        LOGGER.info("ServiceCommandHandlerImpl.pushIntoEventStore");
        return serviceCommandEventStore.pushIntoEventStore(data);
    }

    @Override
    public void updateCommandStatus(CommandStatus status) {
        // Este método comprueba la actualizacion de estado. Podríamos guardar el estado, etc...
        LOGGER.info("ServiceCommandHandlerImpl updated status: " + status.getDescription());
    }
}
