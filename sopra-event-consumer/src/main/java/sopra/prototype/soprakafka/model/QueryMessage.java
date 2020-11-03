package sopra.prototype.soprakafka.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryMessage {
    // date creation of pushed aggregation
    private long timestamp;
    // aggregation message
    private String message;
    // date creation of user
    private String dateRegister;
    // username
    private String name;
}
