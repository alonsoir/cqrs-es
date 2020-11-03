package es.sopra.prototype.patterns.soprapatterns.observable;

import es.sopra.prototype.patterns.soprapatterns.observer.ServiceCommandObserver;
import es.sopra.prototype.patterns.soprapatterns.status.CommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceCommandObservable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCommandObservable.class);

    private final List<ServiceCommandObserver> observers = new ArrayList<ServiceCommandObserver>();

    public void addObserver(ServiceCommandObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(ServiceCommandObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers(CommandStatus currentStatus) {
        for (ServiceCommandObserver obs : observers) {
            LOGGER.info("notifyObservers. currentStatus {}",currentStatus);
            obs.updateCommandStatus(currentStatus);
        }
    }
}
