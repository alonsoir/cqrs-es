package es.sopra.prototype.services.eventstore;

import es.sopra.prototype.vo.UserData;

public interface ServiceCommandEventStore {
    UserData pushIntoEventStore(UserData data);

}
