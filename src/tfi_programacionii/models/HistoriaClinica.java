package tfi_programacionii.models;

import tfi_programacionii.enums.GrupoSanguineo;
import java.util.Objects;

public class HistoriaClinica extends Base {
    private String nroHistoria;
    private GrupoSanguineo grupoSanguineo ;
    private String antecedentes;
    private String medicacionActual;
    private String observaciones;
    
    // CONSTRUCTOR VACIO
    public HistoriaClinica() {
        super();
        this.nroHistoria = "PENDIENTE";
        this.grupoSanguineo = GrupoSanguineo.O_NEGATIVO;
        this.antecedentes = "";
        this.medicacionActual = "";
        this.observaciones = "";
    }
    
    // CONSTRUCTOR PARA NUEVAS HISTORIAS CLINICAS: SIN ID (GENERADO AUTOMATICAMENTE POR DB)
    public HistoriaClinica(String nroHistoria, GrupoSanguineo grupoSanguineo, String antecedentes, String medicacionActual,
                           String observaciones) {
        super();
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
    }
    
    // CONSTRUCTOR PARA OBTENER HISTORIAS CLINICAS EXISTENTES
    public HistoriaClinica(int id, boolean eliminado, String nroHistoria, GrupoSanguineo grupoSanguineo, String antecedentes, String medicacionActual,
                           String observaciones) {
        super(id, eliminado);
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
    }
    
    public String getNroHistoria() {
        return nroHistoria;
    }

    public void setNroHistoria(String nroHistoria) {
        this.nroHistoria = nroHistoria;
    }

    public GrupoSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getMedicacionActual() {
        return medicacionActual;
    }

    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    @Override
    public boolean equals(Object o) {
        // ES LA MISMA INSTANCIA
        if (this == o) return true;
        
        // SI ES NULO O NO ES LA MISMA CLASE NO SON IGUALES
        if (o == null || getClass() != o.getClass()) return false;
        
        // SI LA PARTE HEREDADA DE LA INSTANCIA NO ES IGUAL A LA PARTE HEREDADA DE o
        if (!super.equals(o)) return false;
        
        // COMPARO SI SON IGUALES CON ID Y DNI
        HistoriaClinica historiaClinica = (HistoriaClinica)o;
        return Objects.equals(getId(), historiaClinica.getId()) && Objects.equals(nroHistoria, historiaClinica.getNroHistoria()); // USO Objects PARA EVITAR NullPointerException
    }
    
    @Override
    public int hashCode() {
        // GENERO UN HASH COMBINANDO ID Y NROHISTORIA
        return Objects.hash(getId(), nroHistoria);
    }
    
    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "numero= " + getNroHistoria() +
                ", grupo_sanguineo= " + getGrupoSanguineo() +
                ", antecedentes= " + getAntecedentes() +
                ", medicacion= " + getMedicacionActual() + 
                ", observaciones= " + getObservaciones() +
                "}";
    }
}
