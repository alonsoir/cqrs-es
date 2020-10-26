package es.sopra.prototype.services.status;

/***
 * Este enumerado va a describir el estado por el que va a poder pasar cada interaccion con ServiceCommandHandler.
 * Invoked -> He invocado a ServiceCommandHandler. Estado inicial
 * SavedIntoDB -> He conseguido guardar el comando en BD.
 * SavedFailedIntoDB -> No he conseguido guardar el comando en BD.
 * SavedEventStore -> He conseguido guardar el evento en el EventStore
 * SavedFailedEventStore -> No he conseguido guardar el evento en el EventStore.
 * NotifiedToHandler ->Notifico al handler que he acabado. Estado Final.
 */
public enum CommandStatus {

    Initialized("Initialized"),
    Invoked("Invoked"),
    SavedIntoDB("SavedIntoDB"),
    SavedFailedIntoDB("SavedFailedIntoDB"),
    DeletedFromDB("DeletedFromDB"),
    SavedEventStore("SavedEventStore"),
    SavedFailedEventStore("SavedFailedEventStore"),
    NotifiedToHandler("NotifiedToHandler");

    private final String description;

    CommandStatus(final String _description){
        this.description = _description;
    }

    public String getDescription() {
        return description;
    }
    public String toString() {
        return this.name().toString();
    }
}
