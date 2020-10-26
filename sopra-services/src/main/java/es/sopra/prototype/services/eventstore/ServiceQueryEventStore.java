package es.sopra.prototype.services.eventstore;

import es.sopra.prototype.vo.UserData;

public interface ServiceQueryEventStore {
    // Este método será el punto de entrada, sacará eventos del topic de eventos para guardarlos en el cluster de
    // lectura. Habrá que cambiar el pojo de salida, porque será otra cosa distinta a la entidad de base de datos.
    UserData poll();

}
