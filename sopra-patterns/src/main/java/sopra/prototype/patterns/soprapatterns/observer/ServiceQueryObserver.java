package sopra.prototype.patterns.soprapatterns.observer;


import sopra.prototype.patterns.soprapatterns.status.QueryStatus;

public interface ServiceQueryObserver {
    void updateQueryStatus(QueryStatus status);

}
