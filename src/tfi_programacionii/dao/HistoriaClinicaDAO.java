package tfi_programacionii.dao;

import tfi_programacionii.models.HistoriaClinica;
import tfi_programacionii.enums.GrupoSanguineo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoriaClinicaDAO implements IHistoriaClinicaDAO {
    
    // CONSTRUCTOR
    public HistoriaClinicaDAO() {}
    
    // METODO LEER
    @Override
    public HistoriaClinica leer(Connection con, int id) throws SQLException {
        String sql = "SELECT hc.id, hc.eliminado, hc.nro_historia, gs.descripcion AS grupo_sanguineo_desc, " +
                            "hc.antecedentes, hc.medicacion_actual, hc.observaciones " +
                     "FROM historia_clinica hc " +
                     "JOIN grupo_sanguineo gs ON hc.id_grupo_sanguineo = gs.id " +
                     "WHERE hc.id = ?";
        
        HistoriaClinica historiaClinica = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    historiaClinica = mapResultSetToHistoriaClinica(rs);
                }
            }
        }
        return historiaClinica;
    }
    
    // METODO LEER TODOS
    @Override
    public List<HistoriaClinica> leerTodos(Connection con) throws SQLException {
        List<HistoriaClinica> historiasClinicas = new ArrayList<>();
        
        String sql = "SELECT hc.id, hc.eliminado, hc.nro_historia, gs.descripcion AS grupo_sanguineo_desc, " +
                     "hc.antecedentes, hc.medicacion_actual, hc.observaciones " +
                     "FROM historia_clinica hc " +
                     "JOIN grupo_sanguineo gs ON hc.id_grupo_sanguineo = gs.id " +
                     "WHERE hc.eliminado = 0";
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                historiasClinicas.add(mapResultSetToHistoriaClinica(rs));
            }
        }
        return historiasClinicas;
    }
    
    // METODO PARA INSERTAR
    @Override
    public void crear(Connection con, HistoriaClinica historiaClinica) throws SQLException {
        String sql = "INSERT INTO historia_clinica (eliminado, nro_historia, id_grupo_sanguineo, antecedentes, " +
                     "medicacion_actual, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, historiaClinica.isEliminado());
            ps.setString(2, historiaClinica.getNroHistoria());
            
            // OBTENGO EL ID DE LA TABLA grupo_sanguineo
            ps.setInt(3, historiaClinica.getGrupoSanguineo().getId());
            ps.setString(4, historiaClinica.getAntecedentes());
            ps.setString(5, historiaClinica.getMedicacionActual());
            ps.setString(6, historiaClinica.getObservaciones());
            
            int registrosActualizados = ps.executeUpdate();
            
            if (registrosActualizados == 0) {
                throw new SQLException("Error al crear historia clinica");
            }
            
            try (ResultSet clavesGeneradas = ps.getGeneratedKeys()) {
                if (clavesGeneradas.next()) {
                    historiaClinica.setId(clavesGeneradas.getInt(1));
                } else {
                    throw new SQLException("Error al crear historia clinica");
                }
            }
        }
    }
    
    // METODO PARA ACTUALIZAR
    @Override
    public void actualizar(Connection con, HistoriaClinica historiaClinica) throws SQLException {
        String sql = "UPDATE historia_clinica " + 
                     "SET eliminado = ?, " +
                     "nro_historia = ?, " +
                     "id_grupo_sanguineo = ?, " +
                     "antecedentes = ?, " +
                     "medicacion_actual = ?, " +
                     "observaciones = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, historiaClinica.isEliminado());
            ps.setString(2, historiaClinica.getNroHistoria());
            ps.setInt(3, historiaClinica.getGrupoSanguineo().getId());
            ps.setString(4, historiaClinica.getAntecedentes());
            ps.setString(5, historiaClinica.getMedicacionActual());
            ps.setString(6, historiaClinica.getObservaciones());
            ps.setInt(7, historiaClinica.getId());
            
            int registrosAfectados = ps.executeUpdate();
            if (registrosAfectados == 0) {
                throw new SQLException("Error al actualizar historia clinica");
            }
        }
    }
    
    // METODO PARA ELIMINAR (BORRADO LOGICO)
    @Override
    public void eliminar(Connection con, int id) throws SQLException {
        String sql = "UPDATE historia_clinica SET eliminado = 1 WHERE id = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            
            int registrosAfectados = ps.executeUpdate();
            if (registrosAfectados == 0) {
                throw new SQLException("Error al eliminar la historia clinica.");
            }
        }
    }
    
    // METODO PARA BUSCAR HISTORIAS CLINICAS CON SU NRO
    @Override
    public HistoriaClinica buscarPorNroHistoria(Connection con, String nroHistoria) throws SQLException {
        String sql = "SELECT hc.id, hc.eliminado, hc.nro_historia, gs.descripcion AS grupo_sanguineo_desc, " +
                            "hc.antecedentes, hc.medicacion_actual, hc.observaciones " +
                     "FROM historia_clinica hc " +
                     "JOIN grupo_sanguineo gs ON hc.id_grupo_sanguineo = gs.id " +
                     "WHERE hc.nro_historia = ?";
        
        HistoriaClinica historiaClinica = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nroHistoria);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    historiaClinica = mapResultSetToHistoriaClinica(rs);
                }
            }
        }
        return historiaClinica;
    }
    
    // METODO AUXILIAR PARA HACER EL MAPEO
    private HistoriaClinica mapResultSetToHistoriaClinica(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        boolean eliminado = rs.getBoolean("eliminado");
        String nroHistoria = rs.getString("nro_historia");
        String grupoSanguineoDesc = rs.getString("grupo_sanguineo_desc");
        String antecedentes = rs.getString("antecedentes");
        String medicacionActual = rs.getString("medicacion_actual");
        String observaciones = rs.getString("observaciones");
        
        GrupoSanguineo grupoSanguineo = GrupoSanguineo.fromAbreviatura(grupoSanguineoDesc);
        
        return new HistoriaClinica(id, eliminado, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones);
    }
}
