package sopra.prototype.soprakafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import sopra.prototype.soprakafka.model.CommandMessage;
import sopra.prototype.soprakafka.model.Greetings;
import sopra.prototype.soprakafka.stream.GreetingsStreams;

@Service
@Slf4j
public class CommandServiceEventStoreImpl implements CommandServiceEventStore {

    private final GreetingsStreams greetingsStreams;
    private MessageChannel messageChannel;

    public CommandServiceEventStoreImpl(GreetingsStreams greetingsStreams) {

        this.greetingsStreams = greetingsStreams;
        MessageChannel messageChannel = greetingsStreams.outboundGreetings();

    }

    public boolean sendCommandMessage(final CommandMessage message){
        log.info("Sendind Command message {}",message);
        return messageChannel.send(MessageBuilder
                .withPayload(message)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
    public void sendGreeting(final Greetings greetings) {
        log.info("Sending greetings {}", greetings);

        //MessageChannel messageChannel = greetingsStreams.outboundGreetings();
        messageChannel.send(MessageBuilder
                .withPayload(greetings)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public void sendSimpleMessage(final String simpleMessage){
        log.info("Sending simple message {}", simpleMessage);
        //MessageChannel messageChannel = greetingsStreams.outboundGreetings();
        messageChannel.send(MessageBuilder
                .withPayload(simpleMessage)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
