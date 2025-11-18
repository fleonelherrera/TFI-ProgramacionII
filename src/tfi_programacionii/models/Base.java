package tfi_programacionii.models;

public abstract class Base {
    private int id;
    private boolean eliminado;
    
    public Base(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }
    
    public Base() {
        this.eliminado = false;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isEliminado() {
        return eliminado;
    }
    
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
