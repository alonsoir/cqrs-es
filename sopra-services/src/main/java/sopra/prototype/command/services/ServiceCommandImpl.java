package sopra.prototype.command.services;

import sopra.prototype.patterns.soprapatterns.observable.ServiceCommandObservable;
import sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de commandos. Tiene que guardar en Base de datos y señalar que ha conseguido guardar
 * en base de datos, o no.
 */
@Service
public class ServiceCommandImpl extends ServiceCommandObservable implements ServiceCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandImpl.class);

    private final UserDataRepository userCommandDataRepository;
    private CommandStatus currentStatus;

    @Autowired
    public ServiceCommandImpl(UserDataRepository _userCommandDataRepository){
        super();
        currentStatus = CommandStatus.Initialized;
        notifyObservers(currentStatus);
        userCommandDataRepository = _userCommandDataRepository;
    }

    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        UserData userSaved = userCommandDataRepository.save(user);
        LOGGER.info("userSaved: " + userSaved.toString());
        boolean isPresent = userCommandDataRepository.existsById(user.getIdUserData());
        LOGGER.info("isPresent?: " + !isPresent);
        currentStatus = CommandStatus.SavedIntoDB;
        notifyObservers(currentStatus);
        return  isPresent;
    }

    @Override
    public boolean deleteFromDB(Integer id) {
        userCommandDataRepository.deleteById(id);
        boolean isDeleted = userCommandDataRepository.existsById(id);
        LOGGER.info("isDeleted?: " + !isDeleted);
        currentStatus = CommandStatus.DeletedFromDB;
        notifyObservers(currentStatus);
        return !isDeleted;
    }

    @Override
    public List<UserData> listAll() {
        return userCommandDataRepository.findAll();
    }

    @Override
    public List<UserData> findByName(String name) {
        return userCommandDataRepository.findByName(name);
    }

    public void observer(){
        // Quien es mi observer? ServiceCommandHandlerImpl
        //this.addObserver();
    }
}
