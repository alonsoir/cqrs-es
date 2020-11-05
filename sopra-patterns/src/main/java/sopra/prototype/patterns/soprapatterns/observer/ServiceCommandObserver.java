package sopra.prototype.patterns.soprapatterns.observer;


import sopra.prototype.patterns.soprapatterns.status.CommandStatus;

/***
 * Esta interfaz va a cumplir el contrato del servicio observado, en este caso ServiceCommand
 */
public interface ServiceCommandObserver {

    void updateCommandStatus(CommandStatus status);
}
