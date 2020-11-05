package sopra.prototype.patterns.soprapatterns.status;

/***
 * Este enumerado va a describir el estado por el que va a poder pasar cada interaccion con ServiceQueryHandler.
 * Invoked -> He invocado a ServiceQueryHandler. Estado inicial.
 * ConsumedFromTopic -> He consumido un evento del EventStore.
 * SavedIntoDB -> He guardado el evento leído del EventStore en BD.
 * SavedFailedIntoDB -> No he conseguido guardar el evento leído del EventStore en BD.
 * NotifiedToHandler -> Notifico al service Handler. Estado Final.
 */
public enum QueryStatus {

    Initialized("Initialized"),
    Invoked("Invoked"),
    ConsumedFromTopic("ConsumedFromTopic"),
    ErrorConsumingFromTopic("ErrorConsumingFromTopic"),
    SavedIntoDB("SavedIntoDB"),
    SavedFailedIntoDB("SavedFailedIntoDB"),
    NotifiedToHandler("NotifiedToHandler");

    private final String description;

    QueryStatus(final String _description){
        this.description = _description;
    }

    public String getDescription() {
        return description;
    }
    public String toString() {
        return this.name().toString();
    }
}
