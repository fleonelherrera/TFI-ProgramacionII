package tfi_programacionii.service;

import tfi_programacionii.config.DatabaseConnection;
import tfi_programacionii.dao.PacienteDAO;
import tfi_programacionii.dao.IPacienteDAO;
import tfi_programacionii.dao.HistoriaClinicaDAO;
import tfi_programacionii.dao.IHistoriaClinicaDAO;
import tfi_programacionii.models.Paciente;
import tfi_programacionii.models.HistoriaClinica;

import java.util.Objects;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

public class PacienteService implements IPacienteService {
    private IPacienteDAO pacienteDao;
    private IHistoriaClinicaDAO historiaClinicaDao;
    
    public PacienteService(IPacienteDAO pacienteDao, IHistoriaClinicaDAO historiaClinicaDao) {
        this.pacienteDao = pacienteDao;
        this.historiaClinicaDao = historiaClinicaDao;
    }
    
    public PacienteService() {
        this(new PacienteDAO(), new HistoriaClinicaDAO());
    }
    
    // METODO PARA INSERTAR PACIENTE
    @Override
    public void insertar(Paciente paciente) throws SQLException {
        // VALIDACIONES
        Objects.requireNonNull(paciente, "El Paciente no puede ser nulo.");
        Objects.requireNonNull(paciente.getNombre(), "El nombre del paciente es obligatorio.");
        Objects.requireNonNull(paciente.getApellido(), "El apellido del paciente es obligatorio.");
        Objects.requireNonNull(paciente.getDni(), "El DNI del paciente es obligatorio.");
        Objects.requireNonNull(paciente.getFechaNacimiento(), "La fecha de nacimiento es obligatoria.");
        Objects.requireNonNull(paciente.getHistoriaClinica(), "La Historia Clínica asociada es obligatoria para el paciente.");
        
        // VALIDACIONES PARA LA HISTORIA CLINICA
        Objects.requireNonNull(paciente.getHistoriaClinica().getNroHistoria(), "El número de historia clínica es obligatorio.");
        Objects.requireNonNull(paciente.getHistoriaClinica().getGrupoSanguineo(), "El grupo sanguíneo de la HC es obligatorio.");
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);
            
            // VALIDO QUE EL DNI NO EXISTA
            Paciente pacienteExistente = pacienteDao.buscarPorDni(con, paciente.getDni());
            if (pacienteExistente != null && !pacienteExistente.isEliminado()) {
                throw new SQLException("Ya existe un Paciente con el DNI " + paciente.getDni());
            }

            // VALIDO QUE EL NRO DE LA HISTORIA CLINICA NO EXISTA
            HistoriaClinica hcExistente = historiaClinicaDao.buscarPorNroHistoria(con, paciente.getHistoriaClinica().getNroHistoria());
            if (hcExistente != null && !hcExistente.isEliminado()) {
                throw new SQLException("Ya existe una Historia Clínica con el número " + paciente.getHistoriaClinica().getNroHistoria());
            }
            
            historiaClinicaDao.crear(con, paciente.getHistoriaClinica());
            pacienteDao.crear(con, paciente);
            
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
    
    // METODO PARA OBTENER PACIENTE POR ID
    @Override
    public Paciente getById(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            Paciente paciente = pacienteDao.leer(con, id);
            return paciente;
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
    
    // METODO PARA OBTENER TODOS LOS PACIENTES
    @Override
    public List<Paciente> getAll() throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            List<Paciente> pacientes = pacienteDao.leerTodos(con);
            return pacientes;
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
    
    // METODO PARA ACTUALIZAR UN PACIENTE
    @Override
    public void actualizar(Paciente paciente) throws SQLException {
        Objects.requireNonNull(paciente, "El Paciente no puede ser nulo.");
        Objects.requireNonNull(paciente.getId(), "El ID del Paciente es obligatorio para actualizar.");
        Objects.requireNonNull(paciente.getNombre(), "El nombre del paciente es obligatorio.");
        Objects.requireNonNull(paciente.getApellido(), "El apellido del paciente es obligatorio.");
        Objects.requireNonNull(paciente.getDni(), "El DNI del paciente es obligatorio.");
        Objects.requireNonNull(paciente.getFechaNacimiento(), "La fecha de nacimiento es obligatoria.");
        Objects.requireNonNull(paciente.getHistoriaClinica(), "La Historia Clínica asociada es obligatoria para el paciente.");
        Objects.requireNonNull(paciente.getHistoriaClinica().getId(), "El ID de la Historia Clínica asociada es obligatorio para actualizar.");
        Objects.requireNonNull(paciente.getHistoriaClinica().getNroHistoria(), "El número de historia clínica es obligatorio.");
        Objects.requireNonNull(paciente.getHistoriaClinica().getGrupoSanguineo(), "El grupo sanguíneo de la HC es obligatorio.");
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);
            
            // VALIDO QUE EL PACIENTE EXISTA
            Paciente pacienteExistente = pacienteDao.leer(con, paciente.getId());
            if (pacienteExistente == null) {
                throw new SQLException("El Paciente con ID " + paciente.getId() + " no existe.");
            }

            // VALIDO QUE SI SE MODIFICA EL DNI, NO COINCIDA CON EL DE OTRO PACIENTE
            Paciente pacienteConMismoDni = pacienteDao.buscarPorDni(con, paciente.getDni());
            if (pacienteConMismoDni != null && pacienteConMismoDni.getId() != paciente.getId() && !pacienteConMismoDni.isEliminado()) {
                throw new SQLException("Ya existe otro Paciente con el DNI " + paciente.getDni());
            }

            // VALIDO QUE SI SE MODIFICA EL NRO DE HISTORIA, NO COINCIDA CON EL DE OTRO PACIENTE
            HistoriaClinica hcExistente = historiaClinicaDao.buscarPorNroHistoria(con, paciente.getHistoriaClinica().getNroHistoria());
            if (hcExistente != null && hcExistente.getId() != paciente.getHistoriaClinica().getId() && !hcExistente.isEliminado()) {
                throw new SQLException("Ya existe otra Historia Clínica con el número " + paciente.getHistoriaClinica().getNroHistoria());
            }
            
            historiaClinicaDao.actualizar(con, paciente.getHistoriaClinica());
            pacienteDao.actualizar(con, paciente);
            
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
    
    @Override
    public void eliminar(int id) throws SQLException {
        // VALIDO QUE EL ID SEA VALIDO
        if (id <= 0) {
            throw new SQLException("El ID del Paciente no es válido.");
        }
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);
            
            Paciente pacienteExistente = pacienteDao.leer(con, id);
            if (pacienteExistente == null) {
                throw new SQLException("El Paciente con ID " + id + " no existe.");
            }
            if (pacienteExistente.isEliminado()) {
                throw new SQLException("El Paciente con ID " + id + " ya está eliminado.");
            }
            
            historiaClinicaDao.eliminar(con, pacienteExistente.getHistoriaClinica().getId());
            pacienteDao.eliminar(con, id);
            
            con.commit();
            
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // <<-- Deshace la transacción
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
    
    @Override
    public Paciente buscarPorDni(String dni) throws SQLException {
        Objects.requireNonNull(dni, "El DNI del paciente no puede ser nulo.");
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            Paciente paciente = pacienteDao.buscarPorDni(con, dni);
            return paciente;
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
