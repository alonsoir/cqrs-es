package sopra.prototype.services.impl.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/***
 * Esta clase contendrá métodos que he detectado que están repetidos.
 * TODO esta funcionalidad está en muchos sitios. Tantas que habría que llevarla a un proyecto sopra-utils.
 */
public class SopraUtils {

    public static String getActualFormatedDate() {
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
