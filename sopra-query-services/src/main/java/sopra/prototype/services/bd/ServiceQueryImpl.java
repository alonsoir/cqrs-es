package sopra.prototype.services.bd;

import org.springframework.transaction.annotation.Transactional;
import sopra.prototype.patterns.soprapatterns.observable.ServiceQueryObservable;
import sopra.prototype.patterns.soprapatterns.status.QueryStatus;
import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * TODO
 * Este servicio, uno guay de verdad, sería en realidad dos, uno que solo hiciera expusiera consultas y otro que solo
 * expusiera escrituras. Este de escrituras sería el que estaría inyectado al consumidor de eventos para alimentar asi
 * al cluster de lecturas. El de lecturas sería el que estaría inyectado a los clientes que necesitaran consumir esa
 * informacion Agregada.
 *
 * Por motivos de necesitar más tiempo para otras tareas, dejo constancia para asi poder hacerlo bien en caso de
 * tener que llevar a producción algo asi.
 */
@Service
public class ServiceQueryImpl extends ServiceQueryObservable implements ServiceQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceQueryImpl.class);

    private final UserDataRepository userQueryDataRepository;
    private QueryStatus currentStatus;

    @Autowired
    public ServiceQueryImpl(UserDataRepository _userQueryDataRepository) {
        super();
        currentStatus = QueryStatus.Initialized;
        this.userQueryDataRepository = _userQueryDataRepository;
    }

    @Override
    public List<UserData> listAll() {
        LOGGER.info("listAll...");
        List<UserData> userDatas = new ArrayList<>();
        currentStatus = QueryStatus.Invoked;
        notifyObservers(currentStatus);
        userQueryDataRepository.findAll().forEach(userDatas::add);
        LOGGER.info("listAll. {}",userDatas.size());
        return userDatas;
    }

    @Override
    public UserData getById(Integer id) {
        LOGGER.info("getById. {}",id);
        UserData user = userQueryDataRepository.findById(id).orElse(null);
        currentStatus = QueryStatus.Invoked;
        notifyObservers(currentStatus);
        LOGGER.info("getById. {}",user.toString());
        return user;
    }


    @Override
    @Transactional
    public boolean saveOrUpdateIntoDB(UserData user) {
        //TODO usar el Try.of para capturar la excepcion.
        LOGGER.info("saveOrUpdateIntoDB. {}",user.toString());
        UserData userSaved = userQueryDataRepository.save(user);
        LOGGER.info("userSaved: " + userSaved.toString());
        boolean isPresent = userQueryDataRepository.existsById(user.getIdUserData());
        LOGGER.info("isPresent?: " + !isPresent);
        currentStatus = QueryStatus.SavedIntoDB;
        notifyObservers(currentStatus);
        return  isPresent;
    }

    @Override
    @Transactional
    public boolean delete(UserData user) {
        LOGGER.info("delete. {}",user.toString());

        userQueryDataRepository.delete(user);
        Optional<UserData> possibleUser = userQueryDataRepository.findById(user.getIdUserData());
        boolean isPresent = possibleUser.isPresent();
        LOGGER.info("deleted?. {}",!isPresent);
        return !isPresent;
    }

    @Override
    public List<UserData> findByName(String name) {

        return userQueryDataRepository.findByName(name);
    }
}
