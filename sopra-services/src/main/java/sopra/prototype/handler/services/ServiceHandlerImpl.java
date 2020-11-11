package sopra.prototype.handler.services;

import sopra.prototype.command.handler.services.ServiceCommandHandler;
import sopra.prototype.vo.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceHandlerImpl implements ServiceCommandHandler, ServiceQueryOnly {

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
    public List<UserData> findByName(String name) {
        return serviceCommandHandler.findByName(name);
    }

    @Override
    public UserData getById(Integer id) {
        LOGGER.info("ServiceHandlerImpl.getById");
        return serviceQueryHandler.getById(id);
    }

    @Override
    public boolean saveOrUpdateIntoDB(UserData user) {
        LOGGER.info("ServiceHandlerImpl.saveOrUpdateIntoDB");
        return serviceCommandHandler.saveOrUpdateIntoDB(user);
    }

    @Override
    public boolean deleteFromDB(Integer id) {
        LOGGER.info("ServiceHandlerImpl.deleteFromDB");
        return serviceCommandHandler.deleteFromDB(id);
    }

}
