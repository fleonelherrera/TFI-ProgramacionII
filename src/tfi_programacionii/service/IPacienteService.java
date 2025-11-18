package tfi_programacionii.service;

import tfi_programacionii.models.Paciente;
import java.sql.SQLException;

public interface IPacienteService extends IGenericService<Paciente> {
    Paciente buscarPorDni(String dni) throws SQLException;
}
