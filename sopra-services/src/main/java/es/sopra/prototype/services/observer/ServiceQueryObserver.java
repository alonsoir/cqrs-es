package es.sopra.prototype.services.observer;

import es.sopra.prototype.services.status.QueryStatus;

public interface ServiceQueryObserver {
    void updateQueryStatus(QueryStatus status);

}
