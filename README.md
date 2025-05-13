## CQRS/ES
Un ejemplo sobre como usar el patrón CQRS/ES en java. Separar las escrituras y las lecturas que harías en un entorno distribuido escalable asíncrono, usando dos bases de datos distintas, en este ejemplo dos instancias MariaDB y un motor de streaming de eventos para transmitir cuando ocurre una escritura. La lectura se creará o se consumirá en su instancia de lecturas cuando se haya hecho commit en el cluster de escritura y en el motor de streaming.
Lo ideal sería tener contenedores docker al menos para separar la lógica escritura y de la lectura, así como tener otro contenedor para un broker kafka, así como Zookeeper. Idealmente habría que tener en cuenta que con esta implementación podría pasar que puede que consigamos escribir en la base de datos de escrituras, pero fracasemos al pushear el mensaje en el motor de eventos al estar no disponible, o que fracasemos a la hora de escribir en el base de datos de lectura una vez hemos consumido la agregación del motor de eventos. Esas situaciones hay que tenerlas en cuenta en una situación real.

## PREREQUISITOS

## Plataforma Confluent para tener KAFKA
MariaDB
JDK8
Maven

## Como usar

Levantar plataforma Confluent, normalmente la ip del broker será 0.0.0.0:9092.

Si tienes que cambiarla, busca la propiedad spring.kafka.producer.bootstrap-servers del application.properties del modulo sopra-spring.
Hay otras propiedas de kafka divididas entre el application-command.properties, otras en el application-query.properties.
Habría que crear una instancia spring-cloud-config para unificar estas properties.


Levantar MariaDB.

Hay que bajar, instalar y configurar MariaDB

'''
bash

brew install mariadb

# start MariaDB Server
mysql.server start

# or autostart it (optional if above dont apply)
brew services start mariadb

# 
mariadb-secure-installation

#log in as your user
mysql

#Or log in as root 
mysql -u root -p

#password is set as root

# upgrade mariadb, if you have to
brew update
brew upgrade mariadb
'''

Una vez que has hecho log, con el usuario root/root, ejecuta estos dos scripts:

'''
sql

CREATE DATABASE commands;

CREATE TABLE IF NOT EXISTS commands.USER_DATA (
    ID_USER_DATA INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DATE_REGISTER VARCHAR(255) NOT NULL
)  ENGINE=INNODB;

insert into commands.USER_DATA(ID_USER_DATA, NAME, DATE_REGISTER)
VALUES (1, 'usuario1', '27/10/2020');
COMMIT;
sqlCREATE DATABASE queries;

CREATE TABLE IF NOT EXISTS queries.USER_DATA (
    ID_USER_DATA INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DATE_REGISTER VARCHAR(255) NOT NULL
)  ENGINE=INNODB;

insert into commands.USER_DATA(ID_USER_DATA, NAME, DATE_REGISTER)
VALUES (1, 'usuario1', '27/10/2020');
COMMIT;
'''

Son dos bases de datos, dos esquemas distintos, el código está preparado para funcionar con dos máquinas distintas.
Por motivos de simplicidad, creé el mismo esquema en mi máquina, en dos bases de datos distintas, commands y queries.
Si quieres cambiar su manera de acceder a ellas, ve a los ficheros application-command.properties y application-query-properties.
Están en sopra-services y sopra-query-services.

Ejecutar mvn clean install en la raiz del proyecto, y luego ejecutar el test de integración sopra.prototype.services.impl.ServiceFAKECommandHandlerTest alojado en el directorio de tests del modulo sopra-services.

## Histórico
CQRS/ES es un patrón de arquitectura que trata de segregar las escrituras y las lecturas en clusters distintos.
Cuando escribimos en el cluster de escritura, inmediatamente escribimos en un EventStore, algo como una base de datos
orientada a series temporales, o algo como un log distribuido dicha agregación, es decir, el comando escrito, para
luego ser consumido y guardado en el cluster de lectura para posteriores consultas por otro servicio.
Para ello voy a definir programaticamente la creacion de sendos datasources, por comodidad inicial voy a trabajar con
Mariadb, con dos bases de datos, commands y queries, cada uno con su tabla USER_DATA, tal y como está en el arquetipo
inicial.

## Cosas por hacer

Separar el proyecto en dos, uno para los comandos, otro para las queries.
Añadir soporte Docker a cada uno de los proyectos.
Añadir soporte TestContainer para no tener que levantar la plataforma Confluent y MariaDB en local.
Externalizar las properties en un spring-cloud-config.
Pensar en algún mecanismo de paso de mensaje para emular el patrón Observer/Observable distribuido.
Añadir algún Controller para mostrar el funcionamiento más allá de un test de integración.
Añadir mayor robustez a la hora de controlar el commit distribuido entre los distintos subsistemas de bases de datos así como el motor de eventos.

## Análisis crítico y limitaciones actuales
Puntos fuertes del proyecto

Implementación base del patrón CQRS/ES con separación clara de responsabilidades
Uso de Kafka como event store, aprovechando sus garantías de ordenamiento y persistencia
Conciencia de los desafíos de sistemas distribuidos (identificación de puntos de fallo)
Base sólida para demostraciones conceptuales y experimentación

## Limitaciones y áreas de mejora
## Arquitectura y Diseño

Ausencia del patrón Outbox: No se implementa un mecanismo transaccional para garantizar la consistencia entre la escritura en BD y la publicación del evento.
Idempotencia limitada: Falta un mecanismo robusto para garantizar procesamiento único de mensajes en el consumidor.
Acoplamiento temporal: El sistema requiere disponibilidad simultánea de componentes para funcionar correctamente.

## Resiliencia y Tolerancia a Fallos

Gestión de reintentos básica: No se implementan políticas sofisticadas de reintento con backoff exponencial.
Ausencia de DLQ (Dead Letter Queue): No hay mecanismo para gestionar mensajes que no pueden procesarse después de múltiples reintentos.
Recuperación limitada: Falta estrategia clara para recuperación tras fallos prolongados o particiones de red.

## Operaciones y Observabilidad

Observabilidad limitada: Ausencia de métricas, trazas y logs estructurados para diagnóstico operativo.
Configuración estática: Parámetros operativos mayormente codificados, limitando adaptabilidad en producción.
Ausencia de health checks: No hay endpoints para verificar el estado del sistema y sus componentes.

## Testing

Cobertura principalmente "happy path": Faltan tests para escenarios de fallo y recuperación.
Ausencia de pruebas de integración exhaustivas: No se prueban interacciones completas entre todos los componentes.
Falta de pruebas de estrés y caos: No hay validación del comportamiento bajo condiciones extremas o de fallo.

## Garantías de Entrega

Sólo estrategia AT LEAST ONCE: La implementación actual sólo garantiza entrega eventual, sin mecanismos para evitar duplicación.
Sin soporte para AT MOST ONCE: No se implementan mecanismos de deduplicación en el consumidor.
Falta correlación de mensajes: Ausencia de IDs de correlación para rastrear flujos de eventos relacionados.

## Conclusión y Futuros Desarrollos
Este proyecto proporciona una base sólida para entender los conceptos fundamentales de CQRS y Event Sourcing. Sin embargo, para un sistema de producción real, se necesitaría abordar las limitaciones mencionadas anteriormente.

## En futuros proyectos, se buscarán implementar:

Garantías transaccionales robustas usando patrones como Outbox para asegurar la consistencia entre la base de datos y el bus de eventos.
Estrategias duales de entrega (AT LEAST ONCE y AT MOST ONCE) como opciones configurables según el caso de uso.
Mecanismos avanzados de resiliencia con reintentos parametrizables, circuit breakers y dead letter queues.
Observabilidad integrada con métricas detalladas y trazas distribuidas para diagnóstico operativo.
Testing exhaustivo incluyendo pruebas de caos que simulen fallos de componentes y particiones de red.
Despliegue basado en contenedores con Docker y orquestación para facilitar escalado y recuperación automática.

Este proyecto representa un primer paso valioso en el camino hacia sistemas distribuidos robustos, y las lecciones aprendidas serán fundamentales para las próximas implementaciones.
