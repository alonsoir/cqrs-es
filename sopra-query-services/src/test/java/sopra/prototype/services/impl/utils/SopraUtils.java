package sopra.prototype.services.impl.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/***
 * Esta clase contendrá métodos que he detectado que están repetidos.
 */
public class SopraUtils {

    public static String getActualFormatedDate() {
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }
}
