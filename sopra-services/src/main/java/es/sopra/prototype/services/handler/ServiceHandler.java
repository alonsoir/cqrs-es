package es.sopra.prototype.services.handler;

import es.sopra.prototype.vo.UserData;

import java.util.List;

/***
 * Esta interfaz define el contrato del servicio encargado de interacccionar con ServiceCommandHandler y
 * ServiceQueryHandler. Por comodidad, para este poc, voy a hacer que este servicio provea
 * capacidad para escribir y para leer.
 */
public interface ServiceHandler extends ServiceCommandHandler, ServiceQueryHandler{


}
