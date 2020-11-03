package sopra.prototype.soprakafka.model;

public interface ServiceReceiver {
    boolean getMessageFromTopic() throws InterruptedException;
}
