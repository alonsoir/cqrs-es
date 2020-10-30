package sopra.prototype.soprakafka.service;

import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.model.Greetings;

public interface CommandServiceEventStore {
     boolean sendCommandMessage(final CommandMessage message);
     void sendGreeting(final Greetings greetings) ;
     void sendSimpleMessage(final String simpleMessage);
}
