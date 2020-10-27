package es.sopra.prototype.services.impl;

import es.sopra.prototype.repositories.UserDataRepository;
import es.sopra.prototype.services.bd.ServiceQuery;
import es.sopra.prototype.services.observable.ServiceQueryObservable;
import es.sopra.prototype.services.status.QueryStatus;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ServiceQueryImpl extends ServiceQueryObservable implements ServiceQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandImpl.class);

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
        List<UserData> userDatas = new ArrayList<>();
        userDataRepository.findAll().forEach(userDatas::add);
        return userDatas;
    }

    @Override
    public UserData getById(Long id) {
        return userDataRepository.findById(id).orElse(null);
    }

    @Override
    public UserData saveOrUpdateIntoDB(UserData user) {
        UserData userSaved = userDataRepository.save(user);
        LOGGER.info("userSaved: " + userSaved.toString());
        currentStatus = QueryStatus.SavedIntoDB;
        return  userSaved;
    }
}
