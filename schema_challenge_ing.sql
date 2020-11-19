-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema javistepa_banking
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `javistepa_banking` ;

-- -----------------------------------------------------
-- Schema javistepa_banking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `javistepa_banking` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `javistepa_banking` ;

-- -----------------------------------------------------
-- Table `productos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `productos` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `productos` (
  `id_producto` INT NOT NULL AUTO_INCREMENT,
  `nombre_producto` VARCHAR(150) NOT NULL,
  `precio` DECIMAL(9,2) NOT NULL,
  `iva` INT NULL,
  `tipo` VARCHAR(30) NOT NULL COMMENT 'acción, fondo de inversión, plan de pensiones, hipoteca, crédito, bono, opción, warrant.\n',
  `fecha_inicio_vigencia` DATE NULL,
  `fecha_fin_vigencia` DATE NULL,
  PRIMARY KEY (`id_producto`),
  UNIQUE INDEX `nombre_producto_UNIQUE` (`nombre_producto` ASC) VISIBLE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ciudades`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ciudades` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ciudades` (
  `id_ciudad` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id_ciudad`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `clientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `clientes` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `tipo_documentacion` VARCHAR(50) NOT NULL,
  `documentacion` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `primer_apellido` VARCHAR(100) NOT NULL,
  `segundo_apellido` VARCHAR(100) NULL,
  `provincia` VARCHAR(100) NULL,
  `codigo_postal` VARCHAR(5) NULL,
  `piso` VARCHAR(10) NULL,
  `num_via` INT UNSIGNED NULL,
  `via` VARCHAR(100) NULL,
  `email` VARCHAR(100) NULL,
  `id_ciudad` INT NOT NULL,
  PRIMARY KEY (`id_cliente`),
  INDEX `FK_ID_CIUDAD_idx` (`id_ciudad` ASC) VISIBLE,
  CONSTRAINT `FK_ID_CIUDAD`
    FOREIGN KEY (`id_ciudad`)
    REFERENCES `ciudades` (`id_ciudad`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `gestores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gestores` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `gestores` (
  `id_gestor` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  `oficina` INT NOT NULL,
  PRIMARY KEY (`id_gestor`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `compras`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compras` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `compras` (
  `id_compra` BIGINT NOT NULL AUTO_INCREMENT,
  `unidades` INT UNSIGNED NOT NULL,
  `fecha_compra` TIMESTAMP NOT NULL,
  `id_producto` INT NOT NULL,
  `id_cliente` INT NOT NULL,
  `id_gestor` INT NULL,
  `estado` VARCHAR(45) NULL COMMENT 'OK, ERROR',
  `descripcion_error` VARCHAR(250) NULL,
  `num_reintentos` INT UNSIGNED NULL DEFAULT 0,
  `erase_date` TIMESTAMP NULL,
  PRIMARY KEY (`id_compra`),
  INDEX `FK_ID_PRODUCTO_idx` (`id_producto` ASC) VISIBLE,
  INDEX `FK_ID_CLIENTE_idx` (`id_cliente` ASC) VISIBLE,
  INDEX `FK_ID_GESTOR_idx` (`id_gestor` ASC) VISIBLE,
  CONSTRAINT `FK_ID_PRODUCTO`
    FOREIGN KEY (`id_producto`)
    REFERENCES `productos` (`id_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ID_CLIENTE`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `clientes` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ID_GESTOR`
    FOREIGN KEY (`id_gestor`)
    REFERENCES `gestores` (`id_gestor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
