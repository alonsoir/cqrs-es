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
