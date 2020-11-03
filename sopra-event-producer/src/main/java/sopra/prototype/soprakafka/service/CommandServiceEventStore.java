package sopra.prototype.soprakafka.service;

import sopra.prototype.soprakafka.model.CommandMessage;

public interface CommandServiceEventStore {
     boolean sendCommandMessage(final CommandMessage message);
}
