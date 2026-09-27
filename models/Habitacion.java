package models;

public class Habitacion {
    
    public enum TipoHabitacion {
        CONSULTA_EXTERNA, 
        OBSERVACION, 
        HOSPITALIZACION, 
        QUIROFANO
    }

    private String numero;
    private TipoHabitacion tipo;
    private boolean ocupado;

    public Habitacion(String numero, TipoHabitacion tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.ocupado = false;
    }

    public String getNumero() { return numero; }
    public TipoHabitacion getTipo() { return tipo; }
    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
}