package tfi_programacionii.dao;

import tfi_programacionii.models.HistoriaClinica;
import java.sql.Connection;
import java.sql.SQLException;

public interface IHistoriaClinicaDAO extends IGenericDAO<HistoriaClinica> {
    // METODO PARA BUSCAR HISTORIAS CLINICAS CON SU NRO.
    HistoriaClinica buscarPorNroHistoria(Connection con, String nroHistoria) throws SQLException;
}
