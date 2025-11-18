package tfi_programacionii.service;

import java.util.List;
import java.sql.SQLException;

public interface IGenericService<T> {
    
    // INSERTAR NUEVA ENTIDAD
    void insertar(T entidad) throws SQLException;
    
    // OBTENER ENTIDAD POR ID
    T getById(int id) throws SQLException;
    
    // OBTENER TODAS LAS ENTIDADES
    List<T> getAll() throws SQLException;
    
    // ACTUALIZAR ENTIDAD
    void actualizar(T entidad) throws SQLException;
    
    // ELIMINAR ENTIDAD
    void eliminar(int id) throws SQLException;
    
}
