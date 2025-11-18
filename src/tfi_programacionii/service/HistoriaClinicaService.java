package tfi_programacionii.service;

import tfi_programacionii.config.DatabaseConnection;
import tfi_programacionii.dao.HistoriaClinicaDAO;
import tfi_programacionii.dao.IHistoriaClinicaDAO;
import tfi_programacionii.models.HistoriaClinica;

import java.util.Objects;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;


public class HistoriaClinicaService implements IHistoriaClinicaService {
    private IHistoriaClinicaDAO historiaClinicaDao;
    
    public HistoriaClinicaService(IHistoriaClinicaDAO historiaClinicaDao) {
        this.historiaClinicaDao = historiaClinicaDao;
    }
    
    public HistoriaClinicaService() {
        this(new HistoriaClinicaDAO());
    }
    
    // METODO PARA OBTENER HISTORIA CLINICA POR ID
    @Override
    public HistoriaClinica getById(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            HistoriaClinica historiaClinica = historiaClinicaDao.leer(con, id);
            return historiaClinica;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexion: " + e.getMessage());
                }
            }
        }
    }
    
    // METODO PARA OBTENER TODAS LAS HISTORIAS CLINICAS
    @Override
    public List<HistoriaClinica> getAll() throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            List<HistoriaClinica> historiasClinicas = historiaClinicaDao.leerTodos(con);
            return historiasClinicas;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexion: " + e.getMessage());
                }
            }
        }
    }
    
    // METODO PARA INSERCION
    @Override
    public void insertar(HistoriaClinica historiaClinica) throws SQLException {
        // VALIDACIONES
        Objects.requireNonNull(historiaClinica, "La Historia Clínica no puede ser nula.");
        Objects.requireNonNull(historiaClinica.getNroHistoria(), "El número de historia es obligatorio.");
        Objects.requireNonNull(historiaClinica.getGrupoSanguineo(), "El grupo sanguíneo es obligatorio.");
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // INICIO TRANSACCION
            
            HistoriaClinica hcExistente = historiaClinicaDao.buscarPorNroHistoria(con, historiaClinica.getNroHistoria());
            if (hcExistente != null && !hcExistente.isEliminado()) {
                throw new SQLException("Ya existe una historia clinica con el numero " + hcExistente.getNroHistoria());
            }
            
            historiaClinicaDao.crear(con, historiaClinica);
            con.commit(); // ENVIO LOS CAMBIOS
            
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // VUELVO ATRAS LOS CAMBIOS
                } catch (SQLException ex) {
                    System.err.println("Error al realiza rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexion: " + e.getMessage());
                }
            }
        }
    }
    
    // METODO PARA ACTUALIZAR HISTORIA CLINICA
    @Override
    public void actualizar(HistoriaClinica historiaClinica) throws SQLException {
        // VALIDACIONES
        Objects.requireNonNull(historiaClinica, "La Historia Clínica no puede ser nula.");
        Objects.requireNonNull(historiaClinica.getId(), "El ID de la Historia Clínica es obligatorio para actualizar.");
        Objects.requireNonNull(historiaClinica.getNroHistoria(), "El número de historia es obligatorio.");
        Objects.requireNonNull(historiaClinica.getGrupoSanguineo(), "El grupo sanguíneo es obligatorio.");
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);
            
            // VALIDO QUE EL ID EXISTE EN LA BD
            HistoriaClinica hcExistente = historiaClinicaDao.leer(con, historiaClinica.getId());
            if (hcExistente == null ) {
                throw new SQLException("Error al actualizar historia clinica. No se encuentra la historia con el id " + historiaClinica.getId());
            }
            
            // VALIDO QUE NO SE ACTUALICE EL NROHISTORIA A UNO YA EXISTENTE
            HistoriaClinica hcConMismoNro = historiaClinicaDao.buscarPorNroHistoria(con, historiaClinica.getNroHistoria());
            if (hcConMismoNro != null && hcConMismoNro.getId() != historiaClinica.getId() && !hcConMismoNro.isEliminado()) {
                throw new SQLException("Ya existe una historia clinica con el número " + historiaClinica.getNroHistoria());
            }
            
            historiaClinicaDao.actualizar(con, historiaClinica);
            con.commit();
            
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al realizar rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexion: " + e.getMessage());
                }
            }
        }
    }
    
    // METODO PARA ELIMINAR HISTORIA CLINICA
    @Override
    public void eliminar(int id) throws SQLException {
        if (id <= 0) {
            throw new SQLException("El ID de la historia clinica no es valido.");
        }
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);
            
            // VALIDO QUE LA HISTORIA CLINICA EXISTA O NO ESTE ELIMINADA
            HistoriaClinica hcExistente = historiaClinicaDao.leer(con, id);
            if (hcExistente == null) {
                throw new SQLException("La historia clinica con el id " + id + " no existe.");
            }
            if (hcExistente.isEliminado()) {
                throw new SQLException("La historia clinica con el id " + id + " ya ha sido eliminada.");
            }
            
            historiaClinicaDao.eliminar(con, id);
            con.commit();
            
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al realizar rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }
    
    // BUSCAR HISTORIA CLINICA POR NRO
    @Override
    public HistoriaClinica buscarPorNroHistoria(String nroHistoria) throws SQLException {
        Objects.requireNonNull(nroHistoria, "El número de historia no puede ser nulo.");
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            HistoriaClinica historiaClinica = historiaClinicaDao.buscarPorNroHistoria(con, nroHistoria);
            return historiaClinica;

        } catch (SQLException e) {
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
        
    }
}
