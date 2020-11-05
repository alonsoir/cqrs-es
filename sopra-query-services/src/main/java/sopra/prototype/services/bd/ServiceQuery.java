package sopra.prototype.services.bd;

import sopra.prototype.vo.UserData;

/***
 * Para guardar y a la vez leer del cluster de lectura.
 */
public interface ServiceQuery extends ServiceQueryOnly{


    //Alguien tiene que guardar los datos en el cluster de lectura
    boolean saveOrUpdateIntoDB(UserData user);

}
