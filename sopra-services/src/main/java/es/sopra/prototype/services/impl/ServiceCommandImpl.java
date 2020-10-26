package es.sopra.prototype.services.impl;

import es.sopra.prototype.repositories.UserDataRepository;
import es.sopra.prototype.services.bd.ServiceCommand;
import es.sopra.prototype.services.status.CommandStatus;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del servicio de commandos. Tiene que guardar en Base de datos y señalar que ha conseguido guardar
 * en base de datos, o no.
 */
@Service
public class ServiceCommandImpl implements ServiceCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandImpl.class);

    private final UserDataRepository userDataRepository;
    private CommandStatus currentStatus;
    private final List<CommandStatus> observers;

    @Autowired
    public ServiceCommandImpl(UserDataRepository _userDataRepository){
        observers = new ArrayList<CommandStatus>();
        currentStatus = CommandStatus.Initialized;
        userDataRepository = _userDataRepository;
    }

    @Override
    public UserData saveOrUpdateIntoDB(UserData user) {
        UserData userSaved = userDataRepository.save(user);
        LOGGER.info("userSaved: " + userSaved.toString());
        currentStatus = CommandStatus.SavedIntoDB;
        return  userSaved;
    }

    @Override
    public boolean deleteFromDB(Long id) {
        userDataRepository.deleteById(id);
        boolean isDeleted = userDataRepository.existsById(id);
        LOGGER.info("isDeleted?: " + !isDeleted);
        currentStatus = CommandStatus.DeletedFromDB;
        return !isDeleted;
    }
}
