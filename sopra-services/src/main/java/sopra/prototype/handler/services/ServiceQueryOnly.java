package sopra.prototype.handler.services;

import org.springframework.data.repository.query.Param;
import sopra.prototype.vo.UserData;

import java.util.List;

public interface ServiceQueryOnly {
    // Pongamos que estos métodos tan naive van a ser los que tendría este servicio.
    List<UserData> listAll();

    UserData getById(Integer id);
    List<UserData> findByName(@Param("name")String name);

}
