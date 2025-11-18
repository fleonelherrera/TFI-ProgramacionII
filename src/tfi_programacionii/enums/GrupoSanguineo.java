package tfi_programacionii.enums;

import java.util.Objects;

public enum GrupoSanguineo {
    A_POSITIVO("A+", 1),
    A_NEGATIVO("A-", 2),
    B_POSITIVO("B+", 3),
    B_NEGATIVO("B-", 4),
    AB_POSITIVO("AB+", 5),
    AB_NEGATIVO("AB-", 6),
    O_POSITIVO("O+", 7),
    O_NEGATIVO("O-", 8);
    
    private final String abreviatura;
    private final int id;
    
    GrupoSanguineo(String abreviatura, int id) {
        this.abreviatura = abreviatura;
        this.id = id;
    }
    
    public String getAbreviatura() {
        return abreviatura;
    }
    
    public int getId() {
        return id;
    }
    
    // BUSCA GRUPO SANGUINEO POR ABREVIATURA
    public static GrupoSanguineo fromAbreviatura(String abreviatura) {
        Objects.requireNonNull(abreviatura, "La abreviatura no puede ser nula.");
        for (GrupoSanguineo gs : GrupoSanguineo.values()) {
            if (gs.getAbreviatura().equalsIgnoreCase(abreviatura)) {
                return gs;
            }
        }
        throw new IllegalArgumentException("Abreviatura de Grupo Sanguineo no valida: '" + abreviatura + "'.");
    }
    
    // BUSCA GRUPO SANGUINEO POR ID
    public static GrupoSanguineo fromId(int id) {
        for (GrupoSanguineo gs : GrupoSanguineo.values()) {
            if (gs.getId() == id) {
                return gs;
            }
        }
        throw new IllegalArgumentException("ID de Grupo Sanguineo no valido: '" + id + "'.");
    }
}
