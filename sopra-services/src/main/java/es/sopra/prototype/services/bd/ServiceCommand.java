package es.sopra.prototype.services.bd;

import es.sopra.prototype.vo.UserData;

/***
 * Esta interfaz define el contrato para saber como interaccionar con la bd de escrituras. Super naive, por ahora.
 * En principio, quiero guardar, actualizar o borrar. Para crear la agreación en el Event Store, en caso de borrado,
 * necesitaré poder buscar al usuario por su id.
 */
public interface ServiceCommand {

    UserData saveOrUpdateIntoDB(UserData user);
    boolean deleteFromDB(Long id);


}
