package es.sopra.prototype.services.impl;

import es.sopra.prototype.services.handler.ServiceCommandHandler;
import es.sopra.prototype.services.handler.ServiceQueryHandler;
import es.sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceHandlerImpl implements ServiceCommandHandler, ServiceQueryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHandlerImpl.class);
    
    private final ServiceCommandHandler serviceCommandHandler;
    private final ServiceQueryHandler serviceQueryHandler;

   @Autowired
   public ServiceHandlerImpl(ServiceCommandHandler serviceCommandHandler,ServiceQueryHandler serviceQueryHandler ){
       this.serviceCommandHandler = serviceCommandHandler;
       this.serviceQueryHandler = serviceQueryHandler;
   }
    @Override
    public List<UserData> listAll() {
        LOGGER.info("ServiceHandlerImpl.listAll");
        return serviceQueryHandler.listAll();
    }

    @Override
    public UserData getById(Long id) {
        LOGGER.info("ServiceHandlerImpl.getById");
        return serviceQueryHandler.getById(id);
    }

    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        LOGGER.info("ServiceHandlerImpl.saveOrUpdateIntoDB");
        return serviceCommandHandler.saveOrUpdateIntoDB(user);
    }

    @Override
    public boolean deleteFromDB(Long id) {
        LOGGER.info("ServiceHandlerImpl.deleteFromDB");
        return serviceCommandHandler.deleteFromDB(id);
    }

}
