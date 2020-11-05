package sopra.prototype.soprakafka.service;

import sopra.prototype.soprakafka.model.QueryMessage;

public interface QueryServiceEventStore {

    void consume(final QueryMessage queryMessage);
}
