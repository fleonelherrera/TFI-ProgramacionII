-- CARGA MASIVA DE DATOS DE PRUEBA (1.000 Registros)
DROP TEMPORARY TABLE IF EXISTS digitos1;
DROP TEMPORARY TABLE IF EXISTS digitos2;
DROP TEMPORARY TABLE IF EXISTS digitos3;
DROP TEMPORARY TABLE IF EXISTS numeros;

CREATE TEMPORARY TABLE digitos1 (d INT);
INSERT INTO digitos1 VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9);

CREATE TEMPORARY TABLE digitos2 (d INT);
INSERT INTO digitos2 VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9);

CREATE TEMPORARY TABLE digitos3 (d INT);
INSERT INTO digitos3 VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9);

CREATE TEMPORARY TABLE numeros (n INT PRIMARY KEY);

INSERT INTO numeros (n)
SELECT
d1.d * 100 + d2.d * 10 + d3.d AS numero
FROM digitos1 d1
CROSS JOIN digitos2 d2
CROSS JOIN digitos3 d3
HAVING numero > 0 AND numero <= 1000;

-- INSERTAMOS 1.000 REGISTROS EN historia_clinica
INSERT INTO historia_clinica(nro_historia, id_grupo_sanguineo, antecedentes, medicacion_actual, observaciones)
SELECT
CONCAT('HC', LPAD(n, 3, '0'))           AS nro_historia      ,
(n % 8) + 1                             AS id_grupo_sanguineo, -- Siempre genera del 1 al 8
CONCAT('Antecedente genérico ', n)      AS antecedentes      ,
CONCAT('Medicación genérica ' , n)      AS medicacion_actual ,
CONCAT('Observación genérica ', n)      AS observaciones
FROM numeros
LIMIT 1000;

-- INSERTAMOS 1.000 REGISTROS EN paciente
INSERT INTO paciente (nombre, apellido, dni, fecha_nacimiento, id_historia)
SELECT
CASE (n MOD 11)       WHEN 0 THEN 'Juan'      WHEN 1 THEN 'María'    WHEN 2 THEN 'Carlos'
                      WHEN 3 THEN 'Ana'       WHEN 4 THEN 'Franco'   WHEN 5 THEN 'Mateo'
                      WHEN 6 THEN 'Agustina'  WHEN 7 THEN 'Nicolas'  WHEN 8 THEN 'Sofia'
                      WHEN 9 THEN 'Roberto'   WHEN 10 THEN 'Luis'    END AS nombre,
CASE ((n * 7) MOD 11) WHEN 0 THEN 'Rodriguez' WHEN 1 THEN 'Gonzalez' WHEN 2 THEN 'Martinez'
					  WHEN 3 THEN 'Blanco'    WHEN 4 THEN 'Romero'   WHEN 5 THEN 'Herrera'
					  WHEN 6 THEN 'Fernandez' WHEN 7 THEN 'Gomez'    WHEN 8 THEN 'Lopez'
                      WHEN 9 THEN 'Diaz'      WHEN 10 THEN 'Perez'   END AS apellido,
LPAD(30000000 + n, 8, '0')                                                          AS dni,
-- Fecha de nacimiento ya no usa @seed
DATE_SUB(CURDATE(), INTERVAL FLOOR(18*365 + RAND() * 62*365) DAY) AS fecha_nacimiento,
n                                                                                   AS id_historia
FROM numeros
LIMIT 1000;