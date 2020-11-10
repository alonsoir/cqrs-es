Este proyecto contiene servicios básicos para interactuar con los distintos repositorios y subsistemas.

Este proyecto hace las consultas al cluster de escrituras, junto con el handler. Actualmente la base de datos está 
definida en application-command.properties. 

Actualmente, el arquetipo TAMBIÉN carga el application.properties del proyecto sopra-spring. Si encuentra alguna
 definida allí, usa la propiedad definida allí. Procura comentarlas.

Básate en el sopra-query-services. Hay que crear paquetes de tests, etc...

Ahora mismo está interactuando con un productor de eventos, como una base de datos de comandos.
Ahora mismo está interactuando con un consumidor de eventos y una base de datos de consultas. QUITAR! 

en proceso quitar todo lo relativo al consumidor y la bd de consultas.