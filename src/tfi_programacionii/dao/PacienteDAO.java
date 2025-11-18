package tfi_programacionii.dao;

import tfi_programacionii.models.Paciente;
import tfi_programacionii.models.HistoriaClinica;

import java.time.LocalDate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements IPacienteDAO {
    
    // ATRIBUTO PARA MANEJAR COMPOSICION
    private IHistoriaClinicaDAO historiaClinicaDao;
    
    public PacienteDAO(IHistoriaClinicaDAO historiaClinicaDao) {
        this.historiaClinicaDao = historiaClinicaDao;
    }
    
    public PacienteDAO() {
        this(new HistoriaClinicaDAO());
    }
    
    // METODO PARA LEER
    @Override
    public Paciente leer(Connection con, int id) throws SQLException {
        String sql = "SELECT p.id, p.eliminado, p.nombre, p.apellido, p.dni, p.fecha_nacimiento, p.id_historia " +
                     "FROM paciente p " +
                     "WHERE p.id = ? " +
                     "AND   p.eliminado = 0";
        
        Paciente paciente = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paciente = mapResultSetToPaciente(con, rs);
                }
            }
        }
        return paciente;
    }
    
    // METODO PARA LEER TODOS
    @Override
    public List<Paciente> leerTodos(Connection con) throws SQLException {
        String sql = "SELECT p.id, p.eliminado, p.nombre, p.apellido, p.dni, p.fecha_nacimiento, p.id_historia " +
                     "FROM paciente p " +
                     "WHERE p.eliminado = 0";
        
        List<Paciente> pacientes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(con, rs));
            }
        }
        return pacientes;
    }
    
    // METODO PARA INSERTAR
    @Override
    public void crear(Connection con, Paciente paciente) throws SQLException {
        
        String sql = "INSERT INTO paciente(eliminado, nombre, apellido, dni, fecha_nacimiento, id_historia) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, paciente.isEliminado());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getDni());
            ps.setDate(5, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            ps.setInt(6, paciente.getHistoriaClinica().getId());
            
            int registrosAfectados = ps.executeUpdate();
            if (registrosAfectados == 0) {
                throw new SQLException("Error al crear el paciente");
            }
            
            // OBTENGO EL ID GENERADO POR LA BD Y LO ASIGNO AL PACIENTE
            try (ResultSet clavesGeneradas = ps.getGeneratedKeys()) {
                if (clavesGeneradas.next()) {
                    paciente.setId(clavesGeneradas.getInt(1));
                } else {
                    throw new SQLException("Error al obtener el id del paciente");
                }
            }
        }
    }
    
    // METODO PARA ACTUALIZAR
    @Override
    public void actualizar(Connection con, Paciente paciente) throws SQLException {
        // PACTUALIZO LA HISTORIA CLINICA ASOCIADA
        historiaClinicaDao.actualizar(con, paciente.getHistoriaClinica());
        
        String sql = "UPDATE paciente " +
                     "SET eliminado = ?, " +
                     "nombre = ?, " +
                     "apellido = ?, " +
                     "dni = ?, " +
                     "fecha_nacimiento = ?, " +
                     "id_historia = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, paciente.isEliminado());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getDni());
            ps.setDate(5, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            ps.setInt(6, paciente.getHistoriaClinica().getId());
            ps.setInt(7, paciente.getId()); // PARA EL WHERE
            
            int registrosAfectados = ps.executeUpdate();
            if (registrosAfectados == 0) {
                throw new SQLException("Error al actualizar el paciente.");
            }
        }
    }
    
    // METODO PARA ELIMINADO LOGICO
    @Override
    public void eliminar(Connection con, int id) throws SQLException {
        // OBTENGO EL ID DE LA HISTORIA CLINICA DEL PACIENTE
        Paciente paciente = leer(con, id);
        if (paciente == null) {
            throw new SQLException("Error al obtener el paciente con ID " + id);
        }
        
        // ELIMINO LA HISTORIA CLINICA DEL PACIENTE
        historiaClinicaDao.eliminar(con, paciente.getHistoriaClinica().getId());
        
        String sql = "UPDATE paciente SET eliminado = 1 WHERE id = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int registrosAfectados = ps.executeUpdate();
            if (registrosAfectados == 0) {
                throw new SQLException("Error al eliminar el paciente.");
            }
        }
    }
    
    // METODO PARA BUSCAR PACIENTE CON DNI
    public Paciente buscarPorDni(Connection con, String dni) throws SQLException {
        String sql = "SELECT p.id, p.eliminado, p.nombre, p.apellido, p.dni, p.fecha_nacimiento, p.id_historia " +
                     "FROM paciente p " +
                     "WHERE p.dni = ?";
        
        Paciente paciente = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paciente = mapResultSetToPaciente(con, rs);
                }
            }
        }
        return paciente;
    }
    
    // METODO AUXILIAR PARA MAPEAR UN RESULTSET A UN PACIENTE
    private Paciente mapResultSetToPaciente(Connection con, ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        boolean eliminado = rs.getBoolean("eliminado");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String dni = rs.getString("dni");
        LocalDate fechaNacimiento = rs.getDate("fecha_nacimiento").toLocalDate();
        int idHistoriaClinica = rs.getInt("id_historia");
        
        HistoriaClinica historiaClinica = historiaClinicaDao.leer(con, idHistoriaClinica);
        if (historiaClinica == null) {
            throw new SQLException("Error: Historia Clinica con ID " + idHistoriaClinica + " no encontrada para el paciente con ID " + id);
        }
        
        // USO EL CONSTRUCTOR PARA OBJETOS EXISTENTES
        return new Paciente(id, eliminado, nombre, apellido, dni, fechaNacimiento, historiaClinica);
    }
}
