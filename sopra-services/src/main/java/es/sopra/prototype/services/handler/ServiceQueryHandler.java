package es.sopra.prototype.services.handler;

import es.sopra.prototype.services.bd.ServiceQuery;
import es.sopra.prototype.services.eventstore.ServiceQueryEventStore;

/***
 * Interfaz que define la interaccion con la base de datos del cluster de lectura y el eventStore.
 */
public interface ServiceQueryHandler extends ServiceQuery, ServiceQueryEventStore {

}
