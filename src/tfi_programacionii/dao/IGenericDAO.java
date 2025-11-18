package tfi_programacionii.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IGenericDAO<T> {
    // INTERFAZ GENERICA QUE DEFINE METODOS COMUNES PARA TRABAJAR CON CUALQUIER ENTIDAD.
    // TODOS LOS METODOS RECIBEN CONEXION EXTERNA, CUMPLIENDO LA CONSIGNA
    
    void crear(Connection con, T entidad) throws SQLException;
    
    T leer(Connection con, int id) throws SQLException;
    
    List<T> leerTodos(Connection con) throws SQLException;
    
    void actualizar(Connection con, T entidad) throws SQLException;
    
    void eliminar(Connection con, int id) throws SQLException;
}
