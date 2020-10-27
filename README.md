Punto inicial, creado por Francisco Javier 
# springboot-sopra

Arquetipo con BBDD Mysql mariaDB y JPA.

### SCRIPT DE LA BBDD

Una vez instalado mariaDB, lanzar las siguientes consultas.

```
CREATE DATABASE arqu_local;

CREATE TABLE IF NOT EXISTS arqu_local.USER_DATA (
    ID_USER_DATA INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DATE_REGISTER VARCHAR(255) NOT NULL
)  ENGINE=INNODB;

insert into arqu_local.USER_DATA(ID_USER_DATA, NAME, DATE_REGISTER)
VALUES (1, 'usuario1', '12/12/2012');
COMMIT;
```

### ENDPOINT PARA PROBAR

```
http://localhost:8001/sopra/userdata/user/1
```

### ENDPOINT DE SWAGGER

```
http://localhost:8001/sopra/swagger-ui.html
```
27 de Octubre 2012

Este proyecto es para implementar CQRS/ES primero como prueba de concepto. 

CQRS/ES es un patrón de arquitectura que trata de segregar las escrituras y las lecturas en clusters distintos.

Cuando escribimos en el cluster de escritura, inmediatamente escribimos en un EventStore, algo como una base de datos 
orientada a series temporales, o algo como un log distribuido dicha agregación, es decir, el comando escrito, para
luego ser consumido y guardado en el cluster de lectura para posteriores consultas por otro servicio.

Para ello voy a definir programaticamente la creacion de sendos datasources, por comodidad inicial voy a trabajar con
Mariadb, con dos bases de datos, commands y queries, cada uno con su tabla USER_DATA, tal y como está en el arquetipo 
inicial. 

base de datos commands:

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

base de datos queries

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