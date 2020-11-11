package sopra.prototype.handler.services;

import sopra.prototype.command.handler.services.ServiceCommandHandler;

/***
 * Esta interfaz define el contrato del servicio encargado de interacccionar con ServiceCommandHandler y
 * ServiceQueryHandler. Por comodidad, para este poc, voy a hacer que este servicio provea
 * capacidad para escribir y para leer.
 */
public interface ServiceHandler extends ServiceCommandHandler, ServiceQueryOnly {


}
