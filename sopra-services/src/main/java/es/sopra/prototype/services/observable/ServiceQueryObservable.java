package es.sopra.prototype.services.observable;

import es.sopra.prototype.services.observer.ServiceCommandObserver;
import es.sopra.prototype.services.observer.ServiceQueryObserver;
import es.sopra.prototype.services.status.QueryStatus;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceQueryObservable {

    private final List<ServiceQueryObserver> observers = new ArrayList<ServiceQueryObserver>();


    public void addObserver(ServiceQueryObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(ServiceCommandObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers(QueryStatus currentStatus) {
        for (ServiceQueryObserver obs : observers) {
            obs.updateQueryStatus(currentStatus);
        }
    }
}
