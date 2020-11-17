package sopra.prototype.services.bd;

import org.apache.catalina.User;
import org.springframework.data.repository.query.Param;
import sopra.prototype.handler.services.ServiceQueryOnly;
import sopra.prototype.vo.UserData;

import java.util.List;

/***
 * Para guardar y a la vez leer del cluster de lectura.
 */
public interface ServiceQuery extends ServiceQueryOnly {


    //Alguien tiene que guardar los datos en el cluster de lectura
    boolean saveOrUpdateIntoDB(UserData user);
    void delete(UserData user);

}
