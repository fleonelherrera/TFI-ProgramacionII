package tfi_programacionii.service;

import tfi_programacionii.models.HistoriaClinica;
import java.sql.SQLException;

public interface IHistoriaClinicaService extends IGenericService<HistoriaClinica> {
    HistoriaClinica buscarPorNroHistoria(String nroHistoria) throws SQLException;
}
