package es.sopra.prototype.services.observable;

import es.sopra.prototype.services.observer.ServiceCommandObserver;
import es.sopra.prototype.services.status.CommandStatus;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceCommandObservable {

    private final List<ServiceCommandObserver> observers = new ArrayList<ServiceCommandObserver>();


    public void addObserver(ServiceCommandObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(ServiceCommandObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers(CommandStatus currentStatus) {
        for (ServiceCommandObserver obs : observers) {
            obs.updateCommandStatus(currentStatus);
        }
    }
}
