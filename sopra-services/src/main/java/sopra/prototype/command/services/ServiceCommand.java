package sopra.prototype.command.services;

import org.springframework.data.repository.query.Param;
import sopra.prototype.vo.UserData;

import java.util.List;

/***
 * Esta interfaz define el contrato para saber como interaccionar con la bd de escrituras. Super naive, por ahora.
 * En principio, quiero guardar, actualizar o borrar. Para crear la agreación en el Event Store, en caso de borrado,
 * necesitaré poder buscar al usuario por su id.
 */
public interface ServiceCommand {

    boolean saveOrUpdateIntoDB(UserData user);
    boolean deleteFromDB(Integer id);

    List<UserData> listAll();
    List<UserData> findByName(@Param("name")String name);



}
