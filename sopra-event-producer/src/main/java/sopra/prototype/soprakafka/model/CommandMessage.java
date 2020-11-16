package sopra.prototype.soprakafka.model;

import lombok.*;

@Data
@Builder
// I need these annotations bellow because of a problema when jackson have to deserialize json through kafka consumer.
// https://stackoverflow.com/questions/51464720/lombok-1-18-0-and-jackson-2-9-6-not-working-together
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CommandMessage {

    // date creation of pushed aggregation
    private long timestamp;
    // aggregation message
    private String message;
    // date creation of user
    private String dateRegister;
    // username
    private String name;
}
