package sopra.prototype.patterns.soprapatterns.observable;


import sopra.prototype.patterns.soprapatterns.observer.ServiceCommandObserver;
import sopra.prototype.patterns.soprapatterns.observer.ServiceQueryObserver;
import sopra.prototype.patterns.soprapatterns.status.QueryStatus;

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
