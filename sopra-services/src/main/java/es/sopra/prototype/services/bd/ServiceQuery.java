package es.sopra.prototype.services.bd;

import es.sopra.prototype.vo.UserData;

import java.util.List;

public interface ServiceQuery {
    // Pongamos que estos métodos tan naive van a ser los que tendría este servicio.
    List<UserData> listAll();

    UserData getById(Long id);

    //Alguien tiene que guardar los datos en el cluster de lectura
    boolean saveOrUpdateIntoDB(UserData user);

}
