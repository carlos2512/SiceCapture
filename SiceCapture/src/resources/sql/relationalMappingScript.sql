-- MySQL Script generated by MySQL Workbench
-- 10/24/16 15:41:05
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Expedient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Expedient` (
  `idExpedient` INT NOT NULL AUTO_INCREMENT COMMENT 'expedient\'s unique identifier',
  `code` VARCHAR(15) NOT NULL COMMENT 'expedient\'s code',
  `name` VARCHAR(30) NOT NULL COMMENT 'expedient\'s name',
  `description` VARCHAR(45) NULL COMMENT 'expedient\'s description',
  `estate` INT NOT NULL COMMENT 'expedient\'s estate',
  PRIMARY KEY (`idExpedient`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Document` (
  `idDocument` INT NOT NULL,
  `code` VARCHAR(15) NOT NULL,
  `name` VARCHAR(35) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`idDocument`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Parameter_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Parameter_type` (
  `idParameter_type` INT NOT NULL,
  `code` VARCHAR(15) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `estate` INT NOT NULL,
  PRIMARY KEY (`idParameter_type`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Document_Parameter`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Document_Parameter` (
  `idParameter` INT NOT NULL,
  `code` VARCHAR(15) NOT NULL,
  `name` VARCHAR(35) NOT NULL,
  `description` VARCHAR(45) NULL,
  `estate` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  `fk_parameter_type` INT NOT NULL,
  `fk_document` INT NOT NULL,
  PRIMARY KEY (`idParameter`, `fk_parameter_type`, `fk_document`),
  INDEX `fk_Document_Parameter_Parameter_type1_idx` (`fk_parameter_type` ASC),
  INDEX `fk_Document_Parameter_Document1_idx` (`fk_document` ASC),
  CONSTRAINT `fk_Document_Parameter_Parameter_type1`
    FOREIGN KEY (`fk_parameter_type`)
    REFERENCES `mydb`.`Parameter_type` (`idParameter_type`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Document_Parameter_Document1`
    FOREIGN KEY (`fk_document`)
    REFERENCES `mydb`.`Document` (`idDocument`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Client` (
  `idUser` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `identification` INT NOT NULL,
  `identification_type` VARCHAR(5) NOT NULL,
  `address` VARCHAR(60) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `cellphone` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Data_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Data_type` (
  `idData_type` INT NOT NULL,
  `code` VARCHAR(15) NOT NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `estate` INT NOT NULL,
  PRIMARY KEY (`idData_type`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Document_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Document_data` (
  `idDocument_data` INT NOT NULL,
  `code` VARCHAR(35) NOT NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `fk_document` INT NOT NULL,
  `required` INT NOT NULL,
  `fk_data_type` INT NOT NULL,
  PRIMARY KEY (`idDocument_data`, `fk_document`, `fk_data_type`),
  INDEX `fk_Document_data_Document1_idx` (`fk_document` ASC),
  INDEX `fk_Document_data_Data_type1_idx` (`fk_data_type` ASC),
  CONSTRAINT `fk_Document_data_Document1`
    FOREIGN KEY (`fk_document`)
    REFERENCES `mydb`.`Document` (`idDocument`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Document_data_Data_type1`
    FOREIGN KEY (`fk_data_type`)
    REFERENCES `mydb`.`Data_type` (`idData_type`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Expedient_Client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Expedient_Client` (
  `fk_expedient` INT NOT NULL,
  `fk_client` INT NOT NULL,
  `last_modification` DATE NOT NULL,
  PRIMARY KEY (`fk_expedient`, `fk_client`),
  INDEX `fk_Expedient_has_Client_Client1_idx` (`fk_client` ASC),
  INDEX `fk_Expedient_has_Client_Expedient_idx` (`fk_expedient` ASC),
  CONSTRAINT `fk_Expedient_has_Client_Expedient`
    FOREIGN KEY (`fk_expedient`)
    REFERENCES `mydb`.`Expedient` (`idExpedient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Expedient_has_Client_Client1`
    FOREIGN KEY (`fk_client`)
    REFERENCES `mydb`.`Client` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Document_Expedient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Document_Expedient` (
  `fk_expedient` INT NOT NULL,
  `fk_document` INT NOT NULL,
  PRIMARY KEY (`fk_expedient`, `fk_document`),
  INDEX `fk_Expedient_has_Document_Document1_idx` (`fk_document` ASC),
  INDEX `fk_Expedient_has_Document_Expedient1_idx` (`fk_expedient` ASC),
  CONSTRAINT `fk_Expedient_has_Document_Expedient1`
    FOREIGN KEY (`fk_expedient`)
    REFERENCES `mydb`.`Expedient` (`idExpedient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Expedient_has_Document_Document1`
    FOREIGN KEY (`fk_document`)
    REFERENCES `mydb`.`Document` (`idDocument`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Client_Data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Client_Data` (
  `fk_client` INT NOT NULL,
  `fk_document_data` INT NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  `last_modification` DATE NOT NULL,
  PRIMARY KEY (`fk_client`, `fk_document_data`),
  INDEX `fk_Client_has_Document_data_Document_data1_idx` (`fk_document_data` ASC),
  INDEX `fk_Client_has_Document_data_Client1_idx` (`fk_client` ASC),
  CONSTRAINT `fk_Client_has_Document_data_Client1`
    FOREIGN KEY (`fk_client`)
    REFERENCES `mydb`.`Client` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Client_has_Document_data_Document_data1`
    FOREIGN KEY (`fk_document_data`)
    REFERENCES `mydb`.`Document_data` (`fk_document`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;