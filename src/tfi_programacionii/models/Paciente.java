package tfi_programacionii.models;

import tfi_programacionii.enums.GrupoSanguineo;
import java.time.LocalDate;
import java.util.Objects;

public class Paciente extends Base {
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private HistoriaClinica historiaClinica;
    
    // CONSTRUCTOR VACIO
    public Paciente() {
        super();
        this.historiaClinica = new HistoriaClinica();
    }
    
    // CONSTRUCTOR PARA NUEVOS PACIENTES: SIN ID (GENERADO AUTOMATICAMENTE POR DB)
    public Paciente(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
                    String nroHistoria, GrupoSanguineo grupoSanguineo, String antecedentes,
                    String medicacionActual, String observaciones) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.historiaClinica = new HistoriaClinica(nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones);
    }
    
    // CONSTRUCTOR PARA OBTENER PACIENTES EXISTENTES
    public Paciente(int id, boolean eliminado, String nombre, String apellido, String dni, LocalDate fechaNacimiento,
                    HistoriaClinica historiaClinica) {
        super(id, eliminado);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.historiaClinica = historiaClinica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
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
        Paciente paciente = (Paciente)o;
        return Objects.equals(getId(), paciente.getId()) && Objects.equals(dni, paciente.getDni()); // USO Objects PARA EVITAR NullPointerException
    }
    
    @Override
    public int hashCode() {
        // GENERO UN HASH COMBINANDO ID Y DNI
        return Objects.hash(getId(), dni);
    }
    
    @Override
    public String toString() {
        return "Paciente{" +
                "nombre= " + getNombre() +
                ", apellido= " + getApellido() +
                ", dni= " + getDni() +
                ", fecha_nacimiento= " + getFechaNacimiento() +
                ", historia_clinica= " + (historiaClinica != null ? historiaClinica.getNroHistoria() : "N/A") +
                "}";
    }
}
