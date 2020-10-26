package es.sopra.prototype.services.observer;

import es.sopra.prototype.services.status.CommandStatus;

/***
 * Esta interfaz va a cumplir el contrato del servicio observado, en este caso ServiceCommand
 */
public interface ServiceCommandObserver {

    void updateCommandStatus(CommandStatus status);
}
