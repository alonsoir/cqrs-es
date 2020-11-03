package es.sopra.prototype.services.impl;

import es.sopra.prototype.patterns.soprapatterns.observer.ServiceQueryObserver;
import es.sopra.prototype.patterns.soprapatterns.status.QueryStatus;
import es.sopra.prototype.services.bd.ServiceQuery;
import es.sopra.prototype.services.handler.ServiceQueryHandler;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sopra.prototype.soprakafka.model.ServiceReceiver;

import java.util.List;

@Service
public class ServiceQueryHandlerImpl implements ServiceQueryHandler, ServiceQueryObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceQueryHandlerImpl.class);

    private final ServiceQuery serviceQuery;
    private final ServiceReceiver serviceReceiver;

    @Autowired
    public ServiceQueryHandlerImpl(ServiceReceiver serviceReceiver,ServiceQuery serviceQuery) {
        this.serviceReceiver = serviceReceiver;
        this.serviceQuery = serviceQuery;
    }

    @Override
    public List<UserData> listAll() {
        LOGGER.info("ServiceQueryHandlerImpl.listAll");
        return serviceQuery.listAll();
    }

    @Override
    public UserData getById(Long id) {
        LOGGER.info("ServiceQueryHandlerImpl.getById: {}" , id);
        return serviceQuery.getById(id);
    }

    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        LOGGER.info("ServiceQueryHandlerImpl.saveOrUpdateIntoDB: {}",user.toString());
        return serviceQuery.saveOrUpdateIntoDB(user);
    }

    public boolean poll() throws InterruptedException {
        LOGGER.info("ServiceQueryHandlerImpl.poll");
        boolean isReceived = serviceReceiver.getMessageFromTopic();
        LOGGER.info("ServiceQueryHandlerImpl.poll {}",isReceived);

        return isReceived;
    }

    @Override
    public void updateQueryStatus(QueryStatus status) {
        LOGGER.info("ServiceQueryHandlerImpl updated status: " + status.getDescription());
    }
}
