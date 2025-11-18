package tfi_programacionii.dao;

import tfi_programacionii.models.Paciente;
import java.sql.Connection;
import java.sql.SQLException;


public interface IPacienteDAO extends IGenericDAO<Paciente> {
    // AGREGAMOS LA POSIBILIDAD DE BUSCAR PACIENTES POR DNI
    Paciente buscarPorDni(Connection con, String dni) throws SQLException;
}
