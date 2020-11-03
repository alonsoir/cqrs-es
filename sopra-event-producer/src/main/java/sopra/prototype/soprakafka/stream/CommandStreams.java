package sopra.prototype.soprakafka.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/***
 * Esto no me gusta, tendría que poder desde el yaml el nombre del topic que quiero poder consumir, y tendría que
 * poder hacerlo de manera automática.
 */
public interface CommandStreams {
    // TODO ojito que esa definicion de topic debe estar en el yaml
    String OUTPUT = "users-event-topic-out";

    @Output(OUTPUT)
    MessageChannel outboundGreetings();
}
