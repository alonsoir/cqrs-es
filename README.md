# Punto inicial, creado por Francisco Javier 
# CQRS/ES, creado por Alonso Isidoro Román.

# Disclaimer, 
    este repositorio es privado, creado en el trabajo como preparación para SOPRA, con el fin de mantener una copia en caso de que el repositorio original o mi     máquina personal donde realicé el trabajo o la copia en el cloud apple asociado a mi cuenta desapareciera por error.

    Ellos, SOPRA Steria ,son los propietarios de este repositorio, el código de la parte CQRS/ES fue creado por mí como dicha preparación. 

### PREREQUISITOS

    plataforma Confluent para tener KAFKA. 
    MariaDB
    JDK8
    Maven
# Como usar

    Levantar plataforma Confluent, normalmente la ip del broker será 0.0.0.0:9092. 
    Si tienes que cambiarla, busca la propiedad spring.kafka.producer.bootstrap-servers del application.properties del modulo sopra-spring.
    Hay otras propiedas de kafka divididas entre el application-command.properties, otras en el application-query.properties. 
    Habría que crear una instancia spring-cloud-config para unificar estas properties.
    
    Levantar MariaDB.
    Hay que bajar, instalar y configurar MariaDB

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
    
    Una vez que has hecho log, con el usuario root/root, ejecuta estos dos scripts:
  
    ```
    CREATE DATABASE commands;

    CREATE TABLE IF NOT EXISTS commands.USER_DATA (
        ID_USER_DATA INT AUTO_INCREMENT PRIMARY KEY,
        NAME VARCHAR(255) NOT NULL,
        DATE_REGISTER VARCHAR(255) NOT NULL
    )  ENGINE=INNODB;

    insert into commands.USER_DATA(ID_USER_DATA, NAME, DATE_REGISTER)
    VALUES (1, 'usuario1', '27/10/2020');
    COMMIT;
    ```

    ```
    CREATE DATABASE queries;

    CREATE TABLE IF NOT EXISTS queries.USER_DATA (
        ID_USER_DATA INT AUTO_INCREMENT PRIMARY KEY,
        NAME VARCHAR(255) NOT NULL,
        DATE_REGISTER VARCHAR(255) NOT NULL
    )  ENGINE=INNODB;

    insert into commands.USER_DATA(ID_USER_DATA, NAME, DATE_REGISTER)
    VALUES (1, 'usuario1', '27/10/2020');
    COMMIT;
    ```


    Son dos bases de datos, dos esquemas distintos, el código está preparado para funcionar con dos máquinas distintas. 
    Por motivos de simplicidad, creé el mismo esquema en mi máquina, en dos bases de datos distintas, commands y queries.
    
    Si quieres cambiar su manera de acceder a ellas, ve a los ficheros application-command.properties y application-query-properties.
    Están en sopra-services y sopra-query-services.
    
    mvn clean install en la raiz del proyecto, ejecutar el test de integración sopra.prototype.services.impl.ServiceFAKECommandHandlerTest alojado en el directorio de tests del modulo sopra-services.
    
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
    
