package sopra.prototype.services.impl;

import sopra.prototype.patterns.soprapatterns.observable.ServiceQueryObservable;
import sopra.prototype.patterns.soprapatterns.status.QueryStatus;
import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.services.bd.ServiceQuery;
import sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ServiceQueryImpl extends ServiceQueryObservable implements ServiceQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceQueryImpl.class);

    private final UserDataRepository userDataRepository;
    private QueryStatus currentStatus;

    @Autowired
    public ServiceQueryImpl(UserDataRepository userDataRepository) {
        super();
        currentStatus = QueryStatus.Initialized;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public List<UserData> listAll() {
        LOGGER.info("listAll...");
        List<UserData> userDatas = new ArrayList<>();
        currentStatus = QueryStatus.Invoked;
        notifyObservers(currentStatus);
        userDataRepository.findAll().forEach(userDatas::add);
        LOGGER.info("listAll. {}",userDatas.size());
        return userDatas;
    }

    @Override
    public UserData getById(Integer id) {
        LOGGER.info("getById. {}",id);
        UserData user = userDataRepository.findById(id).orElse(null);
        currentStatus = QueryStatus.Invoked;
        notifyObservers(currentStatus);
        LOGGER.info("getById. {}",user.toString());
        return user;
    }

    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        //TODO usar el Try.of para capturar la excepcion.
        LOGGER.info("saveOrUpdateIntoDB. {}",user.toString());
        UserData userSaved = userDataRepository.save(user);
        LOGGER.info("userSaved: " + userSaved.toString());
        boolean isPresent = userDataRepository.existsById(user.getIdUserData());
        LOGGER.info("isPresent?: " + !isPresent);
        currentStatus = QueryStatus.SavedIntoDB;
        notifyObservers(currentStatus);
        return  isPresent;
    }
}
