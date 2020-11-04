package es.sopra.prototype.services.impl;

import es.sopra.prototype.patterns.soprapatterns.observable.ServiceCommandObservable;
import es.sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import es.sopra.prototype.repositories.UserDataRepository;
import es.sopra.prototype.services.bd.ServiceCommand;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de commandos. Tiene que guardar en Base de datos y señalar que ha conseguido guardar
 * en base de datos, o no.
 */
@Service
public class ServiceCommandImpl extends ServiceCommandObservable implements ServiceCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandImpl.class);

    private final UserDataRepository userDataRepository;
    private CommandStatus currentStatus;

    @Autowired
    public ServiceCommandImpl(UserDataRepository _userDataRepository){
        super();
        currentStatus = CommandStatus.Initialized;
        userDataRepository = _userDataRepository;
    }

    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        UserData userSaved = userDataRepository.save(user);
        LOGGER.info("userSaved: " + userSaved.toString());
        boolean isPresent = userDataRepository.existsById(user.getIdUserData());
        LOGGER.info("isPresent?: " + !isPresent);
        currentStatus = CommandStatus.SavedIntoDB;
        notifyObservers(currentStatus);
        return  isPresent;
    }

    @Override
    public boolean deleteFromDB(Long id) {
        userDataRepository.deleteById(id);
        boolean isDeleted = userDataRepository.existsById(id);
        LOGGER.info("isDeleted?: " + !isDeleted);
        currentStatus = CommandStatus.DeletedFromDB;
        notifyObservers(currentStatus);
        return !isDeleted;
    }

    public void observer(){
        // Quien es mi observer? ServiceCommandHandlerImpl
        //this.addObserver();
    }
}
