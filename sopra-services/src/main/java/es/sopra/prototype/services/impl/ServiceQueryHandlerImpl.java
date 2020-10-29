package es.sopra.prototype.services.impl;

import es.sopra.prototype.services.bd.ServiceQuery;
import es.sopra.prototype.services.eventstore.ServiceQueryEventStore;
import es.sopra.prototype.services.handler.ServiceQueryHandler;
import es.sopra.prototype.services.observer.ServiceQueryObserver;
import es.sopra.prototype.services.status.QueryStatus;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceQueryHandlerImpl implements ServiceQueryHandler, ServiceQueryObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceQueryHandlerImpl.class);

    private final ServiceQuery serviceQuery;
    private final ServiceQueryEventStore serviceQueryEventStore;

    @Autowired
    public ServiceQueryHandlerImpl(ServiceQueryEventStore serviceQueryEventStore,ServiceQuery serviceQuery) {
        this.serviceQueryEventStore = serviceQueryEventStore;
        this.serviceQuery = serviceQuery;
    }

    @Override
    public List<UserData> listAll() {
        LOGGER.info("ServiceQueryHandlerImpl.");
        return serviceQuery.listAll();
    }

    @Override
    public UserData getById(Long id) {
        LOGGER.info("ServiceQueryHandlerImpl.getById: " + id);
        return serviceQuery.getById(id);
    }

    @Override
    public UserData saveOrUpdateIntoDB(UserData user) {
        LOGGER.info("ServiceQueryHandlerImpl.saveOrUpdateIntoDB: " + user.toString());
        return serviceQuery.saveOrUpdateIntoDB(user);
    }

    @Override
    public UserData poll() {
        LOGGER.info("ServiceQueryHandlerImpl.poll");
        // probablemente el objeto recuperado del EventStore será distinto al entity...
        // habrá que hacer una transformacion seguro para guardar en la base de datos de lecturas.
        // por comodidad voy a hacer que sean el mismo.
        UserData recoveredFromEventStore = serviceQueryEventStore.poll();
        updateQueryStatus(QueryStatus.ConsumedFromTopic);

        LOGGER.info("recoveredFromEventStore: " + recoveredFromEventStore.toString());
        UserData savedPojoIntoQueryCluster = saveOrUpdateIntoDB(recoveredFromEventStore);
        LOGGER.info("savedPojoIntoQueryCluster: " + savedPojoIntoQueryCluster.toString());

        return savedPojoIntoQueryCluster;
    }

    @Override
    public void updateQueryStatus(QueryStatus status) {
        LOGGER.info("ServiceQueryHandlerImpl updated status: " + status.getDescription());
    }
}
