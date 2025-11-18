# Trabajo Final Integrador

## 1. Descripción del Dominio Elegido

El dominio principal del proyecto se centra en la relación de composición 1 a 1 entre un **Paciente** y su **Historia Clínica**.

*   **Entidad A: Paciente**
    *   Es la entidad principal que encapsula y es propietaria de su historia médica.
    *   Atributos: ID (autogenerado), eliminado, nombre, apellido, DNI (único), fecha de nacimiento, y una referencia a su Historia Clínica.
*   **Entidad B: Historia Clínica**
    *   Contiene el historial médico de un Paciente. Su existencia está ligada directamente al Paciente al que pertenece.
    *   Atributos: ID (autogenerado), eliminado, número de historia (único), grupo sanguíneo, antecedentes, medicación actual y observaciones.
*   **Entidad de Catálogo: Grupo Sanguíneo**
    *   Gestiona los diferentes tipos de grupos sanguíneos (ej. 'A+', 'O-') utilizados en las Historias Clínicas.
    *   Atributos: ID, descripción.
*   **Relación Implementada:**
    *   Se estableció una **asociación 1 a 1 unidireccional** explícita desde `Paciente` hacia `HistoriaClínica` (Clase A contiene una referencia a Clase B).
    *   Esta relación se modeló como una **composición estricta** en el código Java: la instancia de `HistoriaClínica` es creada y gestionada por la clase `Paciente`. La vida de la Historia Clínica está completamente ligada a la del Paciente.

## 2. Requisitos del Entorno y Pasos para la Base de Datos

### Requisitos:
*   **Java Development Kit (JDK):** Versión 21 o superior.
*   **Servidor de Base de Datos:** MySQL Server 8.0.44 (o compatible).
*   **Driver JDBC:** MySQL Connector/J 8.4.0
*   **Cliente de Base de Datos:** MySQL Workbench.

### Pasos para Crear y Cargar la Base de Datos:

1.  Ejecute el script **creacion_bd.sql** en MySQL Workbench para la creación de las tablas y la inserción de los grupos sanguíneos.
2.  Ejecute el script **insercion_tablas.sql** para la inserción de datos de prueba
3.  Verifique la correcta inserción consultando a todas las tablas involucradas.

## 3. Configuración de Credenciales de Conexión en Java

Para que la aplicación Java pueda establecer conexión con la base de datos:
1.  Navegue a la ruta `src/resources/` dentro del proyecto.
2.  Abra el archivo `database.properties`.
3.  Edite las líneas `db.user` y `db.password` con las credenciales de su usuario `root` (o el usuario configurado) de MySQL Server 8.0.44:
    ```properties
    db.url=jdbc:mysql://localhost:3306/hospital
    db.user=root             # Usuario de su servidor MySQL
    db.password=             # Contraseña de su usuario MySQL (vacía si no tiene)
    ```

## 4. Compilar y Ejecutar la Aplicación Java

1. Verificar que el `mysql-connector-j-8.4.0.jar` esté correctamente añadido a la sección "Libraries" del proyecto.
2. Realizar una "Clean and Build Project" para compilar todo el código.
3. Hacer click en el botón "Run Project" en la barra de herramientas de NetBeans para ejecutar la aplicación.
4. Flujo básico de uso: La aplicación iniciará mostrando un menú principal con opciones numeradas. Puede interactuar con las opciones para:

* `1. Ingresar nuevo Paciente`: Crea un Paciente y su Historia Clínica asociada, solicitando todos los datos relevantes.
* `2. Buscar Paciente por ID`: Muestra los detalles de un paciente y su HC.
* `3. Listar todos los Pacientes`: Muestra un listado de los pacientes existentes.
* `4. Actualizar datos de Paciente`: Permite modificar la información de un paciente existente.
* `5. Eliminar Paciente`: Realiza una baja lógica del paciente y su HC.
* `6. Buscar Paciente por DNI`: Permite encontrar un paciente por su número de DNI.

## 5. Enlace al Video de Presentación

[(https://drive.google.com/drive/u/0/folders/138kZNmiQyC1ME5o63GhmCx35c0CX4v_k)]