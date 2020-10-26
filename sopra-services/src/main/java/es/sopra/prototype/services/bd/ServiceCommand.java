package es.sopra.prototype.services.bd;

import es.sopra.prototype.vo.UserData;

/***
 * Esta interfaz define el contrato para saber como interaccionar con la bd de escrituras. Super naive, por ahora.
 * En principio, quiero guardar, actualizar o borrar.
 */
public interface ServiceCommand {

    UserData saveOrUpdateIntoDB(UserData user);
    boolean deleteFromDB(Long id);

}
