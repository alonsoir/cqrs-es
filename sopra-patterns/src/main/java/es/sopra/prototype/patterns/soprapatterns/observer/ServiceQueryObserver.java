package es.sopra.prototype.patterns.soprapatterns.observer;


import es.sopra.prototype.patterns.soprapatterns.status.QueryStatus;

public interface ServiceQueryObserver {
    void updateQueryStatus(QueryStatus status);

}
