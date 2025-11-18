CREATE DATABASE IF NOT EXISTS hospital;
USE hospital;

DROP TABLE IF EXISTS paciente;         -- A
DROP TABLE IF EXISTS historia_clinica; -- B
DROP TABLE IF EXISTS grupo_sanguineo;

CREATE TABLE grupo_sanguineo(
   id          INT        PRIMARY KEY AUTO_INCREMENT,
   descripcion VARCHAR(5) NOT NULL UNIQUE
);

CREATE TABLE historia_clinica(
   id                 BIGINT      PRIMARY KEY AUTO_INCREMENT,
   eliminado          TINYINT     DEFAULT 0                 ,
   nro_historia       VARCHAR(20) NOT NULL UNIQUE           ,
   id_grupo_sanguineo INT         NOT NULL                  ,
   antecedentes       TEXT                                  ,
   medicacion_actual  TEXT                                  ,
   observaciones      TEXT                                  ,
   FOREIGN KEY(id_grupo_sanguineo) REFERENCES grupo_sanguineo(id)
);

CREATE TABLE paciente(
   id                  BIGINT       PRIMARY KEY AUTO_INCREMENT,
   eliminado           TINYINT      DEFAULT 0                 ,
   nombre              VARCHAR(80)  NOT NULL                  ,
   apellido            VARCHAR(80)  NOT NULL                  , 
   dni                 VARCHAR(15)  NOT NULL UNIQUE           ,
   fecha_nacimiento    DATE                                   ,
   id_historia         BIGINT       NOT NULL UNIQUE           ,
   FOREIGN KEY(id_historia) REFERENCES historia_clinica(id) ON DELETE CASCADE,
   
   CHECK (dni REGEXP '^[0-9]+$'),
   CHECK (fecha_nacimiento     >= '1900-01-01')
);

ALTER TABLE grupo_sanguineo AUTO_INCREMENT = 1;
ALTER TABLE historia_clinica AUTO_INCREMENT = 1;
ALTER TABLE paciente AUTO_INCREMENT = 1;

-- INSERTO LOS GRUPOS SANGUINEOS
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (1, 'A+');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (2, 'A-');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (3, 'B+');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (4, 'B-');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (5, 'AB+');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (6, 'AB-');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (7, 'O+');
INSERT INTO grupo_sanguineo (id, descripcion) VALUES (8, 'O-');
